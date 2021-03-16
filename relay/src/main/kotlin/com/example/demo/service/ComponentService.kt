package com.example.demo.service

import com.example.demo.data.component.Component

interface ComponentService {

    fun resolveComponentToShow(components: MutableList<Component>): MutableList<Component>?

    fun resolveOneComponentToShow(component: Component): Component

    fun verifyQuantityBeforeInsert(component: Component, method: String): Component

    fun mappingObjectToSend(component: Component, method: String)
}