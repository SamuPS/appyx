package com.github.zsoltk.composeribs.core.routing.source.spotlight.operations

import android.os.Parcelable
import com.github.zsoltk.composeribs.core.routing.RoutingElements
import com.github.zsoltk.composeribs.core.routing.source.spotlight.Spotlight
import com.github.zsoltk.composeribs.core.routing.source.spotlight.Spotlight.TransitionState.ACTIVE
import com.github.zsoltk.composeribs.core.routing.source.spotlight.Spotlight.TransitionState.INACTIVE_AFTER
import com.github.zsoltk.composeribs.core.routing.source.spotlight.Spotlight.TransitionState.INACTIVE_BEFORE
import kotlinx.parcelize.Parcelize


@Parcelize
class Previous<T : Any> : SpotlightOperation<T> {

    override fun isApplicable(elements: RoutingElements<T, Spotlight.TransitionState>) =
        elements.any { it.fromState == INACTIVE_BEFORE }

    override fun invoke(elements: RoutingElements<T, Spotlight.TransitionState>): RoutingElements<T, Spotlight.TransitionState> {
        val previousKey =
            elements.last { it.targetState == INACTIVE_BEFORE }.key

        return elements.map {
            when {
                it.targetState == ACTIVE -> {
                    it.transitionTo(
                        targetState = INACTIVE_AFTER,
                        operation = this
                    )
                }
                it.key == previousKey -> {
                    it.transitionTo(
                        targetState = ACTIVE,
                        operation = this
                    )
                }
                else -> {
                    it
                }
            }
        }
    }
}

fun <T : Parcelable> Spotlight<T, *>.previous() {
    accept(Previous())
}


