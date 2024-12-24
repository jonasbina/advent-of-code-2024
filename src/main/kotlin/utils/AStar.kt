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
fun <Node> aStarSearchAllOptimalPaths(
    start: Node,
    isEnd: (Node) -> Boolean,
    next: (Node) -> Iterable<Pair<Node, Int>>,
    heuristicCostToEnd: (Node) -> Int,
    findLongest: Boolean = false,
): List<AStarAllResults<Node>> {
    val costFromStart: MutableMap<Node, Int> = mutableMapOf(start to 0)
    val estimatedTotalCost: MutableMap<Node, Int> = mutableMapOf(start to heuristicCostToEnd(start))
    val cameFrom: MutableMap<Node, MutableList<Node>> = mutableMapOf(start to mutableListOf())
    val openVertices = PriorityQueue<Node>(
        when {
            findLongest -> compareByDescending { estimatedTotalCost[it] ?: 0 }
            else -> compareBy { estimatedTotalCost[it] ?: Int.MAX_VALUE }
        }
    )

    val optimalEndNodes = mutableListOf<Node>()
    var lowestFoundCost = Int.MAX_VALUE

    openVertices.add(start)

    while (openVertices.isNotEmpty()) {
        val current = openVertices.poll()
        if (costFromStart[current]!! > lowestFoundCost) {
            break
        }
        if (isEnd(current)) {
            val currentCost = costFromStart[current]!!
            if (currentCost < lowestFoundCost) {
                optimalEndNodes.clear()
                lowestFoundCost = currentCost
            }
            if (currentCost == lowestFoundCost) {
                optimalEndNodes.add(current)
            }
        }

        next(current)
            .forEach { (neighbor, cost) ->
                val possibleNewCostToNeighbor = costFromStart[current]?.plus(cost) ?: Int.MAX_VALUE
                val currentCostToNeighbor = costFromStart[neighbor] ?: Int.MAX_VALUE
                if (possibleNewCostToNeighbor <= currentCostToNeighbor) {
                    if (possibleNewCostToNeighbor < currentCostToNeighbor) {
                        cameFrom[neighbor] = mutableListOf(current)
                        costFromStart[neighbor] = possibleNewCostToNeighbor
                    }
                    else {
                        cameFrom.getOrPut(neighbor) { mutableListOf() }.add(current)
                    }
                    estimatedTotalCost[neighbor] =
                        possibleNewCostToNeighbor + heuristicCostToEnd(neighbor)
                    if (neighbor !in openVertices) openVertices.add(neighbor)
                }
            }
    }
    return optimalEndNodes.map { endNode ->
        AStarAllResults(
            path = generateAllPaths(endNode, cameFrom),
            cost = lowestFoundCost
        )
    }
}

// Helper function to generate all possible paths
fun <Node> generateAllPaths(
    end: Node,
    cameFrom: Map<Node, MutableList<Node>>
): List<List<Node>> {
    fun backtrack(current: Node): List<List<Node>> {
        // If no previous nodes, we've reached the start
        if (!cameFrom.containsKey(current) || cameFrom[current]!!.isEmpty()) {
            return listOf(listOf(current))
        }

        // Collect all paths from previous nodes
        return cameFrom[current]!!.flatMap { prev ->
            backtrack(prev).map { it + current }
        }
    }

    return backtrack(end)
}

fun <Node> aStarFindAllPaths(
    start: Node,
    isEnd: (Node) -> Boolean,
    next: (Node) -> Iterable<Pair<Node, Int>>,
    heuristicCostToEnd: (Node) -> Int,
    costLimit: Int = Int.MAX_VALUE
): List<AStarResult<Node>> {
    val paths = ArrayList<AStarResult<Node>>()
    val open = PriorityQueue<PathState<Node>> { a, b -> a.estimatedTotalCost.compareTo(b.estimatedTotalCost) }
    val costs = HashMap<Node, Int>()

    open.offer(PathState(start, 0, heuristicCostToEnd(start), ArrayList<Node>().apply { add(start) }))
    costs[start] = 0

    while (!open.isEmpty()) {
        val curr = open.poll()
        if (curr.costFromStart > costLimit) continue

        if (isEnd(curr.currentNode)) {
            paths.add(AStarResult(curr.path, curr.costFromStart))
            continue
        }

        next(curr.currentNode).forEach { (n, c) ->
            val newCost = curr.costFromStart + c
            if (newCost <= costLimit &&
                newCost <= (costs[n] ?: Int.MAX_VALUE) &&
                !curr.path.contains(n)) {
                val newPath = ArrayList(curr.path).apply { add(n) }
                open.offer(PathState(n, newCost, newCost + heuristicCostToEnd(n), newPath))
                costs[n] = newCost
            }
        }
    }

    return paths.apply { sortBy { it.cost } }
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
data class AStarAllResults<Node>(
    val path: List<List<Node>>,
    val cost: Int
)