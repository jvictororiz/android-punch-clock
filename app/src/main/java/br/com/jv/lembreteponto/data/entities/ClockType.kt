package br.com.jv.lembreteponto.data.entities

sealed class ClockType {
    object Entrada : ClockType()
    object Almoco : ClockType()
    object RetornoAlmoco : ClockType()
    object Saida : ClockType()
    object None : ClockType()

    companion object {
        fun nextClockTypeTime(
            entrada: String?,
            almoco: String?,
            retornoAlmoco: String?,
            saida: String?
        ): ClockType {
            return when {
                entrada == null -> Entrada
                almoco == null -> Almoco
                retornoAlmoco == null -> RetornoAlmoco
                saida == null -> Saida
                else -> None
            }
        }

        fun currentClockTypeTime(
            entrada: String?,
            almoco: String?,
            retornoAlmoco: String?,
            saida: String?
        ): ClockType {
            return when {
                entrada != null && almoco == null -> Entrada
                almoco != null && retornoAlmoco == null -> Almoco
                retornoAlmoco != null && saida == null -> RetornoAlmoco
                saida != null -> Saida
                else -> None
            }
        }

        fun getAllTypes(): List<ClockType> = listOf(Entrada, Almoco, RetornoAlmoco, Saida)
    }
}