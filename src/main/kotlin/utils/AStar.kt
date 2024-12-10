package com.jonasbina.utils

import java.util.*

fun <Node> aStarSearch(
    start: Node,
    isEnd: (Node) -> Boolean,
    next: (Node) -> Iterable<Pair<Node, Int>>,
    heuristicCostToEnd: (Node) -> Int,
    findLongest: Boolean = false,
): AStarResult<Node>? {

    val costFromStart: MutableMap<Node, Int> = mutableMapOf(start to 0)
    val estimatedTotalCost: MutableMap<Node, Int> = mutableMapOf(start to heuristicCostToEnd(start))
    val cameFrom: MutableMap<Node, Node> = mutableMapOf()
    val openVertices = PriorityQueue<Node>(
        when {
            findLongest -> compareByDescending { estimatedTotalCost[it] ?: 0 }
            else -> compareBy { estimatedTotalCost[it] ?: Int.MAX_VALUE }
        }
    )

    openVertices.add(start)

    while (openVertices.isNotEmpty()) {
        val current = openVertices.poll()
        if (isEnd(current)) {
            return AStarResult(
                path = generatePath(current, cameFrom),
                cost = costFromStart[current]!!
            )
        }
        next(current)
            .forEach { (neighbor, cost) ->
                val possibleNewCostToNeighbor = costFromStart[current]?.plus(cost) ?: Int.MAX_VALUE
                val currentCostToNeighbor = costFromStart[neighbor] ?: Int.MAX_VALUE
                if (possibleNewCostToNeighbor < currentCostToNeighbor) {
                    costFromStart[neighbor] = possibleNewCostToNeighbor
                    estimatedTotalCost[neighbor] =
                        possibleNewCostToNeighbor + heuristicCostToEnd(neighbor)
                    cameFrom[neighbor] = current
                    if (neighbor !in openVertices) openVertices.add(neighbor)
                }
            }
    }

    return null
}

fun <Node> aStarSearchReachableGoals(
    start: Node,
    isEnd: (Node) -> Boolean,
    next: (Node) -> Iterable<Pair<Node, Int>>,
    heuristicCostToEnd: (Node) -> Int
): List<AStarResult<Node>> {
    val costFromStart: MutableMap<Node, Int> = mutableMapOf(start to 0)
    val estimatedTotalCost: MutableMap<Node, Int> = mutableMapOf(start to heuristicCostToEnd(start))
    val cameFrom: MutableMap<Node, Node> = mutableMapOf()
    val openVertices = PriorityQueue<Node>(compareBy { estimatedTotalCost[it] ?: Int.MAX_VALUE })
    val foundGoals = mutableListOf<AStarResult<Node>>()

    openVertices.add(start)

    while (openVertices.isNotEmpty()) {
        val current = openVertices.poll()
        if (isEnd(current)) {
            foundGoals.add(
                AStarResult(
                    path = generatePath(current, cameFrom),
                    cost = costFromStart[current]!!
                )
            )
            continue
        }
        next(current).forEach { (neighbor, cost) ->
            val possibleNewCostToNeighbor = costFromStart[current]?.plus(cost) ?: Int.MAX_VALUE
            val currentCostToNeighbor = costFromStart[neighbor] ?: Int.MAX_VALUE

            if (possibleNewCostToNeighbor < currentCostToNeighbor) {
                costFromStart[neighbor] = possibleNewCostToNeighbor
                estimatedTotalCost[neighbor] =
                    possibleNewCostToNeighbor + heuristicCostToEnd(neighbor)
                cameFrom[neighbor] = current

                if (neighbor !in openVertices) openVertices.add(neighbor)
            }
        }
    }

    return foundGoals
}
fun <Node> aStarFindAllPaths(
    start: Node,
    isEnd: (Node) -> Boolean,
    next: (Node) -> Iterable<Pair<Node, Int>>,
    heuristicCostToEnd: (Node) -> Int
): List<AStarResult<Node>> {
    val allPaths = mutableListOf<AStarResult<Node>>()
    val openSet = PriorityQueue<PathState<Node>>(compareBy { it.estimatedTotalCost })
    openSet.add(PathState(start, 0, heuristicCostToEnd(start), listOf(start)))

    while (openSet.isNotEmpty()) {
        val currentState = openSet.poll()

        if (isEnd(currentState.currentNode)) {
            allPaths.add(AStarResult(currentState.path, currentState.costFromStart))
            continue
        }
        for ((neighbor, cost) in next(currentState.currentNode)) {
            val newCostFromStart = currentState.costFromStart + cost
            val newPath = currentState.path + neighbor
            val estimatedCost = newCostFromStart + heuristicCostToEnd(neighbor)
            openSet.add(PathState(neighbor, newCostFromStart, estimatedCost, newPath))
        }
    }

    return allPaths
}

private data class PathState<Node>(
    val currentNode: Node,
    val costFromStart: Int,
    val estimatedTotalCost: Int,
    val path: List<Node>
)


fun <Node> generatePath(end: Node, cameFrom: Map<Node, Node>): List<Node> {
    return generateSequence(end) { cameFrom[it] }.toList().asReversed()
}

class AStarResult<Node>(
    val path: List<Node>,
    val cost: Int,
)
