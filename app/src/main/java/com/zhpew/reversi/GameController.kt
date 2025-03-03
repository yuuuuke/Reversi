package com.zhpew.reversi

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.zhpew.reversi.GameController.Companion.EMPTY_PIECE

class GameController {

    companion object{
        const val EMPTY_PIECE = 0
        const val RED_PIECE = 1
        const val BLUE_PIECE = 2
    }

    val state = mutableStateOf(UiState(Pieces = initCell()))

    private fun initCell():SnapshotStateList<Int>{
        // 初始棋盘上有四个棋子
        val list = ArrayList<Int>(64)
        for(i in 0 until 64){
            list.add(EMPTY_PIECE)
        }
        list[27] = RED_PIECE
        list[28] = BLUE_PIECE
        list[35] = BLUE_PIECE
        list[36] = RED_PIECE
        return list.toMutableStateList()
    }
}

data class UiState(
    val isRedTurn:Boolean = false,
    val Pieces:SnapshotStateList<Int> = SnapshotStateList()
)

data class Piece(
    var state:Int = EMPTY_PIECE,
    var isRed:Boolean = false
)