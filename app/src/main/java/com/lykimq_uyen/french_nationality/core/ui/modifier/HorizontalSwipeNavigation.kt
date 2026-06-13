package com.lykimq_uyen.french_nationality.core.ui.modifier

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private enum class SwipeAxis {
    Undecided,
    Horizontal,
    Vertical,
}

fun Modifier.horizontalSwipeNavigation(
    onSwipeNext: () -> Unit,
    onSwipePrevious: () -> Unit,
    canSwipeNext: Boolean = true,
    canSwipePrevious: Boolean = true,
    swipeThreshold: Dp = 56.dp,
): Modifier {
    return pointerInput(canSwipeNext, canSwipePrevious, swipeThreshold) {
        val thresholdPx = swipeThreshold.toPx()
        val touchSlop = viewConfiguration.touchSlop
        val minFlingVelocity = 400f

        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            val pointerId = down.id
            val velocityTracker = VelocityTracker()
            var axis = SwipeAxis.Undecided
            var totalX = 0f
            var totalY = 0f

            while (true) {
                val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                val change = event.changes.firstOrNull { it.id == pointerId } ?: break

                if (change.changedToUp()) {
                    velocityTracker.addPointerInputChange(change)
                    break
                }

                if (!change.pressed) {
                    continue
                }

                val delta = change.positionChange()
                velocityTracker.addPointerInputChange(change)

                when (axis) {
                    SwipeAxis.Undecided -> {
                        totalX += delta.x
                        totalY += delta.y
                        if (abs(totalX) > touchSlop || abs(totalY) > touchSlop) {
                            axis = if (abs(totalX) > abs(totalY)) {
                                SwipeAxis.Horizontal
                            } else {
                                SwipeAxis.Vertical
                            }
                        }
                    }

                    SwipeAxis.Horizontal -> {
                        change.consume()
                        totalX += delta.x
                    }

                    SwipeAxis.Vertical -> {
                        return@awaitEachGesture
                    }
                }
            }

            if (axis != SwipeAxis.Horizontal) {
                return@awaitEachGesture
            }

            val velocity = velocityTracker.calculateVelocity()
            val swipeNextByDistance = totalX <= -thresholdPx
            val swipePreviousByDistance = totalX >= thresholdPx
            val swipeNextByFling = velocity.x <= -minFlingVelocity &&
                abs(velocity.x) > abs(velocity.y)
            val swipePreviousByFling = velocity.x >= minFlingVelocity &&
                abs(velocity.x) > abs(velocity.y)

            when {
                (swipeNextByDistance || swipeNextByFling) && canSwipeNext -> onSwipeNext()
                (swipePreviousByDistance || swipePreviousByFling) && canSwipePrevious -> {
                    onSwipePrevious()
                }
            }
        }
    }
}

private fun PointerInputChange.changedToUp(): Boolean {
    return !pressed && previousPressed
}
