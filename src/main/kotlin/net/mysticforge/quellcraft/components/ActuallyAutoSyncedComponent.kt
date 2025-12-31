package net.mysticforge.quellcraft.components

import net.minecraft.world.entity.Entity
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import org.ladysnake.cca.api.v3.component.ComponentKey
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class ActuallyAutoSyncedComponent(
    private val entity: Entity,
    private val componentKey: ComponentKey<*>
) : AutoSyncedComponent {
    private val values = mutableListOf<GenericSyncableValue<*>>()

    protected fun <T : Any> autoSyncedValue(key: String, defaultValue: T) =
        SyncableValue(
            key = key,
            value = defaultValue,
            entity = entity,
            componentKey = componentKey,
        )
            .also { values += it }

    @Suppress("DEPRECATION")
    protected inline fun <reified T : Any> autoSyncedOptionalValue(key: String, value: T? = null) =
        autoSyncedOptionalValueImpl(
            key = key,
            value = value?.let(OptionalValue<T>::Set) ?: OptionalValue.Unset(),
            valueTypeClass = T::class,
        )

    @Deprecated(message = "Don't use this dumbass")
    protected fun <T : Any> autoSyncedOptionalValueImpl(
        key: String,
        value: OptionalValue<T>,
        valueTypeClass: KClass<T>,
    ) =
        OptionalSyncableValue(
            key = key,
            value = value,
            valueTypeClass = valueTypeClass,
            entity = entity,
            componentKey = componentKey,
        )
            .also { values += it }

    override fun readData(input: ValueInput) {
        values.forEach {
            it.setFromValueInput(input)
        }

        componentKey.sync(entity)
    }

    override fun writeData(writeView: ValueOutput) {
        values.forEach {
            it.writeToValueOutput(writeView)
        }
    }

    protected abstract class GenericSyncableValue<T : Any>(
        protected val key: String,
        protected var value: T,
        private val entity: Entity,
        private val componentKey: ComponentKey<*>,
    ) {
        open operator fun getValue(thisRef: Any?, property: KProperty<*>) = value

        open operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = value
            componentKey.sync(entity)
        }

        abstract fun setFromValueInput(input: ValueInput)

        abstract fun writeToValueOutput(output: ValueOutput)
    }

    protected class SyncableValue<T : Any>(
        key: String,
        value: T,
        entity: Entity,
        componentKey: ComponentKey<*>,
    ) : GenericSyncableValue<T>(
        key = key,
        value = value,
        entity = entity,
        componentKey = componentKey
    ) {
        override fun setFromValueInput(input: ValueInput) {
            input.getTyped(key, value::class)?.let {
                value = it
            }
        }

        override fun writeToValueOutput(output: ValueOutput) {
            output.setTyped(key, value)
        }
    }

    protected class OptionalSyncableValue<T : Any>(
        key: String,
        value: OptionalValue<T>,
        private val valueTypeClass: KClass<T>,
        entity: Entity,
        componentKey: ComponentKey<*>,
    ) : GenericSyncableValue<OptionalValue<T>>(
        key = key,
        value = value,
        entity = entity,
        componentKey = componentKey
    ) {
        override fun setFromValueInput(input: ValueInput) {
            value = input.getTyped(key, valueTypeClass)
                ?.let { OptionalValue.Set(it) }
                ?: OptionalValue.Unset()
        }

        override fun writeToValueOutput(output: ValueOutput) {
            (value as? OptionalValue.Set)?.let {
                output.setTyped(key, it.value)
            }
        }
    }
}

private fun <T : Any> ValueInput.getTyped(key: String, clazz: KClass<T>): T? =
    when (clazz) {
        Int::class -> getInt(key)
        else -> error("Unsupported type ${clazz.qualifiedName}.")
    }
        .takeIf { it.isPresent }
        ?.let {
            @Suppress("UNCHECKED_CAST")
            it.get() as T
        }

private fun <T : Any> ValueOutput.setTyped(key: String, value: T) =
    when (value) {
        is Int -> putInt(key, value)
        else -> error("Unsupported type ${value::class.qualifiedName}.")
    }

sealed interface OptionalValue<T> {
    class Unset<T> : OptionalValue<T>
    data class Set<T>(val value: T) : OptionalValue<T>

    fun getOrDefault(defaultValue: T) =
        when (this) {
            is Set -> value
            is Unset -> defaultValue
        }
}