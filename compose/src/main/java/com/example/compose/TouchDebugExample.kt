package com.example.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Node
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

// --- Node Implementation ---
private class SiblingSharingPointerNode(var id: String) : Node(), PointerInputModifierNode {

    // ***** THE KEY PART *****
    // Signal that pointer events dispatched to this node should also be
    // dispatched to sibling PointerInputModifierNodes.
    override fun sharePointerInputWithSiblings(): Boolean {
        return true
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        boundsSize: IntSize,
    ) {
        // --- Optional: React to the event ---
        // We only log on the initial pass to avoid duplicate logs for the same event.
        if (pass == PointerEventPass.Initial) {
            val eventSummary = pointerEvent.changes.joinToString {
                "id=${it.id}, pos=${it.position}"
            }
            println(">>> [$id] Pointer Event: Type=${pointerEvent.type}, Pass=$pass, Changes=[$eventSummary]")
        }

        // --- Optional: Consumption ---
        // If one sibling consumes, the others *still* receive the event because
        // sharePointerInputWithSiblings bypasses the normal consumption cancellation
        // for siblings *during the same pass*. However, consumption still prevents
        // the event from propagating to parents or further down the tree in subsequent passes.
        // Example: Uncomment to see 'Sibling A' consume, but 'Sibling B' still logs.
        /*
        if (id == "Sibling A" && pass == PointerEventPass.Main && pointerEvent.type == PointerEventType.Press) {
            pointerEvent.changes.firstOrNull()?.consume()
            println(">>> [$id] Consumed Press in Main Pass")
        }
        */
    }

    override fun onCancelPointerInput() {
        // Called if pointer input dispatch is cancelled (e.g., Composable leaves tree)
        println(">>> [$id] Pointer Input Cancelled")
    }

    // Implement other Modifier.Node methods if needed (onAttach, onDetach, etc.)
}


// --- Element (Factory for the Node) ---
// This makes it easy to apply our Node using standard Modifier syntax.
private data class SiblingSharingPointerElement(
    val id: String,
    private val inspectorInfo: InspectorInfo.() -> Unit,
) : ModifierNodeElement<SiblingSharingPointerNode>() {

    override fun InspectorInfo.inspectableProperties() {
        inspectorInfo()
    }

    override fun create(): SiblingSharingPointerNode = SiblingSharingPointerNode(id)

    override fun update(node: SiblingSharingPointerNode) {
        node.id = id // Update the node if the id changes
    }

    // Implementing equals/hashCode is important for efficient updates
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SiblingSharingPointerElement
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

// --- Public Modifier Extension Function ---
// This is the function developers will use to apply the modifier.
internal fun Modifier.logAndSharePointerInputWithSiblings(id: String): Modifier =
    this then SiblingSharingPointerElement(
        id = id,
        inspectorInfo = debugInspectorInfo {
            name = "SharePointerInputWithSiblings"
            properties["id"] = id
        }
    )

@Preview(showBackground = true)
@Composable
internal fun SiblingPointerSharingDemo() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Sibling Pointer Sharing Demo (using PointerInputModifierNode)")
        Text("Click or Drag within Box A or Box B and observe Logcat output.")
        Text("Logs prefixed with '>>> [Sibling A]' and '>>> [Sibling B]' should appear simultaneously.")

        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
        ) {
            // Sibling 1
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Cyan)
                    .clickable {
                        println(">>> [Sibling A] Clicked")
                    }
            ) {
                Text("Sibling A")
            }

            // Sibling 2
            var circlePosition by remember { mutableStateOf<Offset?>(null) }
            Canvas(
                modifier = Modifier
                    .size(150.dp)
                    // Apply the custom modifier node
                    .logAndSharePointerInputWithSiblings("Sibling B") // Use the extension function
                    .pointerInput(Unit) { // Key Unit: effect doesn't restart unnecessarily
                        // 2. awaitEachGesture handles the loop for each gesture start.
                        awaitEachGesture {
                            // 3. Wait for the first pointer (finger) to touch down.
                            //    requireUnconsumed = true (default) means we only react if the event wasn't already handled.
                            val down: PointerInputChange = awaitFirstDown(
                                pass = PointerEventPass.Initial,
                            )
                            val pointerId = down.id // Track the ID of this specific pointer

                            // Set the initial position and consume the down event.
                            circlePosition = down.position

                            // 4. Enter a loop to track subsequent events (moves, up) for THIS pointer.
                            try {
                                while (true) {
                                    // 5. Wait for the next pointer event.
                                    val event: PointerEvent = awaitPointerEvent(
                                        pass = PointerEventPass.Initial
                                    )

                                    // Find the change associated with the pointer we are tracking.
                                    val change: PointerInputChange? = event.changes.find { it.id == pointerId }

                                    if (change != null) {
                                        // If the pointer is still down (pressed)...
                                        if (change.pressed) {
                                            // Update the position and consume the change.
                                            circlePosition = change.position
                                        } else {
                                            // Pointer went up! Consume the 'up' change and exit the loop.
                                            break // Exit the while loop for this gesture
                                        }
                                    } else {
                                        // Change for our pointer ID not found in this event batch.
                                        // This might happen in complex scenarios or if the pointer is suddenly gone.
                                        // Treat it as if the pointer is up.
                                        break
                                    }
                                }
                            } finally {
                                // 6. Cleanup: This block executes when the while loop exits
                                //    (because the pointer went up) or if the gesture is cancelled
                                //    (e.g., by system interference, causing awaitPointerEvent to throw CancellationException).
                                //    Ensure the circle disappears.
                                circlePosition = null
                            }
                        }
                    }
            ) {
                // 7. Drawing logic: Draw the circle only if position is not null.
                circlePosition?.let { position ->
                    drawCircle(
                        color = Color.Red, // Use a different color
                        radius = 20f,      // Slightly different radius
                        center = position
                    )
                }
            }
        }
        Text("Note: Filter Logcat by 'System.out' or your app's tag.")
    }
}
