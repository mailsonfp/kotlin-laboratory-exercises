package com.mailson.pereira.laboratory.exercises

import java.math.BigDecimal
import java.util.UUID

data class RawTxn(
    val id: String,
    val merchantId: String?,
    val amount: BigDecimal?
)

sealed class Result {
    data class Summary(
        val merchantId: String,
        val total: BigDecimal,
        val count: Int
    ) : Result()

    data class Error(
        val id: String,
        val reason: Reason
    ) : Result()
}

enum class Reason { BAD_ID, BAD_MERCHANT, BAD_AMOUNT }
/*val sample = listOf(
        RawTxn("t1", "m1", BigDecimal("100.00")),
        RawTxn("t2", "m1", BigDecimal("50.00")),
        RawTxn("t3", "m2", BigDecimal("200.00")),
        RawTxn("", "m3", BigDecimal("10.00")),
        RawTxn("t5", null, BigDecimal("15.00")),
        RawTxn("t6", "m4", BigDecimal("-5.00"))
    ) */

fun process(txns: List<RawTxn>): List<Result> {
    val badIdsList = txns.filter { it.id.isBlank() }.map {
        toErrorObject(Reason.BAD_ID)
    }

    val badMerchantIdList = txns.filter { it.merchantId.isNullOrBlank() }.map {
        toErrorObject(Reason.BAD_MERCHANT)
    }
    val badAmountList = txns.filter { it.amount == null || it.amount.compareTo(BigDecimal.ZERO) <= 0 }.map {
        toErrorObject(Reason.BAD_AMOUNT)
    }

    val summary = txns
        .filter { (!it.merchantId.isNullOrBlank()) && (it.id.isNotBlank()) && (it.amount == null || it.amount.compareTo(BigDecimal.ZERO) > 0) }.groupBy{ it.merchantId }
        .map { (merchantId, transactionSum) ->
            Result.Summary(
                merchantId = merchantId!!,
                total = transactionSum.sumOf { it.amount!! },
                count = transactionSum.size
            )
        }

    return summary + badIdsList + badMerchantIdList + badAmountList
}

fun toErrorObject(reason: Reason): Result.Error {
    return Result.Error(UUID.randomUUID().toString(),reason)
}


class TtlCache<K, V>(private val ttlMs: Long) {
    val cacheValue = mutableMapOf<K,V>()
    val lastAcessTimeCache = mutableMapOf<K,Long>()

    fun put(key: K, value: V) {
        cacheValue.put(key,value)
    }

    /**
     * Returns the value if it exists AND has not expired.
     * Reading (get) must refresh the TTL for that key.
     */
    fun get(key: K): V? {
        if(cacheValue.contains(key)) {
            val value = cacheValue[key]
            val lastTimeAccess = lastAcessTimeCache[key] ?: System.currentTimeMillis()
            if(isExpired(lastTimeAccess))
                return null
            else {
                refreshLastTimeAcess(key)
                return value
            }
        } else return null
    }

    private fun refreshLastTimeAcess(key: K) {
        lastAcessTimeCache.put(key, System.currentTimeMillis())
    }

    private fun isExpired(lastTimeAccess: Long) = (System.currentTimeMillis() - lastTimeAccess) > ttlMs
}

fun main() {
    val cache = TtlCache<String, String>(1000)
    cache.put("a", "hello")
    println(cache.get("a"))
    Thread.sleep(1200)
    println(cache.get("a"))  // expected: null

    val sample = listOf(
        RawTxn("t1", "m1", BigDecimal("100.00")),
        RawTxn("t2", "m1", BigDecimal("50.00")),
        RawTxn("t3", "m2", BigDecimal("200.00")),
        RawTxn("", "m3", BigDecimal("10.00")),
        RawTxn("t5", null, BigDecimal("15.00")),
        RawTxn("t6", "m4", BigDecimal("-5.00"))
    )

    println("process of transactions: ${process(sample)}")
}