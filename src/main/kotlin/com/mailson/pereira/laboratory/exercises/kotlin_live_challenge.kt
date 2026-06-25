// Kotlin Live Coding Challenge
// ------------------------------------------------------
// PART A / Transaction Processing
// ------------------------------------------------------
/*
Implement the function below that validates, groups, and summarizes
a list of transactions.

DATA MODELS:

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

TASK:
    fun process(txns: List<RawTxn>): List<Result>

VALIDATION RULES:
    - id is blank. Reason.BAD_ID
    - merchantId is null or blank. Reason.BAD_MERCHANT
    - amount is null or ≤ 0. Reason.BAD_AMOUNT

VALID TXN RULES:
    1. Group by merchantId
    2. Produce a Summary(total = BigDecimal sum, count = number of items)
    3. Sort summaries by total descending

FINAL OUTPUT ORDER:
    1. Summaries first (sorted)
    2. Errors after (any order)

SAMPLE INPUT FOR TESTING:

    val sample = listOf(
        RawTxn("t1", "m1", BigDecimal("100.00")),
        RawTxn("t2", "m1", BigDecimal("50.00")),
        RawTxn("t3", "m2", BigDecimal("200.00")),
        RawTxn("", "m3", BigDecimal("10.00")),
        RawTxn("t5", null, BigDecimal("15.00")),
        RawTxn("t6", "m4", BigDecimal("-5.00"))
    )
*/

// Your implementation here:
// fun process(txns: List<RawTxn>): List<Result> { }

//(txns: List<RawTxn>): List<Result> { }


// ------------------------------------------------------
// PART B / TTL Cache
// ------------------------------------------------------
/*
Implement a simple in-memory TTL cache with expiration.

API TO IMPLEMENT:

    class TtlCache<K, V>(private val ttlMs: Long) {

        fun put(key: K, value: V)

        /**
         * Returns the value if it exists AND has not expired.
         * Reading (get) must refresh the TTL for that key.
         */
        fun get(key: K): V?
    }

FUNCTIONAL REQUIREMENTS:
    - Store value + last access time for each key
    - A key expires when (currentTime - lastAccessTime) > ttlMs
    - get() returns null if expired
    - get() refreshes TTL
    - No background cleanup required
    - Use System.currentTimeMillis()

EXAMPLE TEST:

    val cache = TtlCache<String, String>(1000)
    cache.put("a", "hello")
    println(cache.get("a"))
    Thread.sleep(1200)
    println(cache.get("a"))  // expected: null
*/

// Your implementation here:
// class TtlCache<K, V>(private val ttlMs: Long) {
//     fun put(key: K, value: V) { }
//     fun get(key: K): V? { return null }
// }
//     fun get(key: K): V? { return null }
