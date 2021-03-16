package builder

import com.example.demo.data.component.Component
import org.bson.types.ObjectId

class ComponentBuilder private constructor(
    val id: String? = ObjectId.get().toString(),
    val name: String?,
    val manufacturer: String?,
    val isActive: Boolean?,
    val quantity: Int?,
    val group: String?,
    val value: Double?
) {
    data class Builder(
        var id: String? = null,
        var name: String? = null,
        var manufacturer: String? = null,
        var isActive: Boolean? = null,
        var quantity: Int? = 1,
        var group: String? = null,
        var value: Double? = null
    ) {
        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun manufacturer(manufacturer: String) = apply { this.manufacturer = manufacturer }
        fun isActive(isActive: Boolean) = apply { this.isActive = isActive }
        fun quantity(quantity: Int) = apply { this.quantity = quantity }
        fun group(group: String) = apply { this.group = group }
        fun value(value: Double) = apply { this.value = value }
        fun build() = Component(id, name, manufacturer, isActive, quantity, group, value)
    }
}
