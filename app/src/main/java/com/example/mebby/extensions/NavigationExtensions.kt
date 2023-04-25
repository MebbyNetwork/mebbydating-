package com.example.mebby.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph

fun NavController.navigateSafe(@IdRes actionId: Int, args: Bundle? = null) {
    currentDestination?.let { currentDestination ->
        val navAction = currentDestination.getAction(actionId)
        if (navAction != null) {
            val destinationId = navAction.destinationId
            if (destinationId != 0) {
                val currentNode = currentDestination as? NavGraph ?: currentDestination.parent
                if (currentNode?.findDestination(destinationId) != null) {
                    navigate(actionId, args, null)
                }
            }
        }
    }
}

private fun NavGraph.findDestination(destinationId: Int): NavDestination? {
    if (id == destinationId) return this
    val node = findNode(destinationId)
    if (node != null) return node
    return parent?.findDestination(destinationId)
}