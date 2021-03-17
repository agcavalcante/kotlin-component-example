package builder

import com.example.demo.data.client.Client
import org.bson.types.ObjectId

class ClientBuilder private constructor(
    val id: String? = ObjectId.get().toString(),
    val name: String?,
    val isActive: Boolean?,
    val cpf: String?
) {
    data class Builder(
        var id: String? = null,
        var name: String? = null,
        var isActive: Boolean? = null,
        var cpf: String? = null
    ) {
        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun isActive(isActive: Boolean) = apply { this.isActive = isActive }
        fun cpf(cpf: String) = apply { this.cpf = cpf }
        fun build() = Client(id, name, isActive, cpf)
    }
}
