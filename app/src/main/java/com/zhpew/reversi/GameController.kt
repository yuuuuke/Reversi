package com.zhpew.reversi

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.zhpew.reversi.GameController.Companion.EMPTY_PIECE

class GameController {

    companion object{
        const val EMPTY_PIECE = 0
        const val RED_PIECE = 1
        const val BLUE_PIECE = 2

        const val LEFT = 0
        const val TOP = 1
        const val TOP_LEFT = 2
        const val RIGHT = 3
        const val TOP_RIGHT = 4
        const val BOTTOM = 5
        const val BOTTOM_LEFT = 6
        const val BOTTOM_RIGHT = 7
    }

    val state = mutableStateOf(UiState(Pieces = initCell()))

    fun initGame(){
        state.value = state.value.copy(isRedTurn = false, Pieces = initCell(), RedCount = 2, BlueCount = 2, showEndView = false)
    }

    val onClick : (index:Int)->Unit = {
        //点击，判断上，下，左，右，左上，左下，右上，右下，八个方向是否可以吃掉对方棋子
        if(state.value.Pieces[it] == EMPTY_PIECE){
            state.value.Pieces[it] = if(state.value.isRedTurn) RED_PIECE else BLUE_PIECE
            reversal(it,TOP, ArrayList(8))
            reversal(it,LEFT,ArrayList(8))
            reversal(it,TOP_LEFT,ArrayList(8))
            reversal(it,RIGHT,ArrayList(8))
            reversal(it,BOTTOM,ArrayList(8))
            reversal(it,TOP_RIGHT,ArrayList(8))
            reversal(it,BOTTOM_LEFT,ArrayList(8))
            reversal(it,BOTTOM_RIGHT,ArrayList(8))

            // 判断结束
            var redCount = 0
            var blueCount = 0
            state.value.Pieces.forEach { piece->
                if(piece == RED_PIECE){
                    redCount++
                }else if(piece == BLUE_PIECE){
                    blueCount++
                }
            }
            state.value = state.value.copy(
                isRedTurn = !state.value.isRedTurn,
                RedCount = redCount,
                BlueCount = blueCount,
                showEndView = redCount+blueCount == 64
            )
        }
    }

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

    private fun reversal(index:Int,nextPieceGuide:Int,array:ArrayList<Int>){
        val column = index % 8
        val row = index / 8
        // 判断越界情况
        if(column == 0 || column == 7 || row == 0 || row == 7){
            if(nextPieceGuide == RIGHT || nextPieceGuide == TOP_RIGHT || nextPieceGuide == BOTTOM_RIGHT){
                if(column == 7){
                    return
                }
            }
            if(nextPieceGuide == LEFT || nextPieceGuide == TOP_LEFT || nextPieceGuide == BOTTOM_LEFT){
                if(column == 0){
                    return
                }
            }
            if(nextPieceGuide == TOP || nextPieceGuide == TOP_LEFT || nextPieceGuide == TOP_RIGHT){
                if(row == 0){
                    return
                }
            }
            if(nextPieceGuide == BOTTOM || nextPieceGuide == BOTTOM_LEFT || nextPieceGuide == BOTTOM_RIGHT){
                if(row == 7){
                    return
                }
            }
        }
        val currentPieceRed = state.value.isRedTurn
        val nextIndex = index + getGuideIndex(nextPieceGuide)
        if(state.value.Pieces[nextIndex] == EMPTY_PIECE){
            return
        }
        if(state.value.Pieces[nextIndex] == if(currentPieceRed) RED_PIECE else BLUE_PIECE){
            // 结束了，拿到array里面的数据，全部反转
            array.forEach{ data ->
                state.value.Pieces[data] = if(currentPieceRed) RED_PIECE else BLUE_PIECE
            }
        }else{
            // 还没结束，记录下来
            array.add(element = nextIndex)
            reversal(nextIndex,nextPieceGuide,array)
        }
    }

    private fun getGuideIndex(guide:Int):Int{
        return when(guide){
            RIGHT -> 1
            TOP_RIGHT->-7
            TOP->-8
            TOP_LEFT->-9
            LEFT->-1
            BOTTOM_LEFT->7
            BOTTOM->8
            BOTTOM_RIGHT->9
            else -> 0
        }
    }
}

data class UiState(
    var isRedTurn:Boolean = false,
    val Pieces:SnapshotStateList<Int> = SnapshotStateList(),
    var RedCount:Int = 2,
    var BlueCount:Int = 2,
    var showEndView:Boolean = false
)

data class Piece(
    var state:Int = EMPTY_PIECE,
    var isRed:Boolean = false
)