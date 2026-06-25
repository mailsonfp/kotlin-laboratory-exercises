package com.mailson.pereira.laboratory.exercises

import java.math.BigDecimal
import kotlin.text.iterator

data class Transaction(val clientId: Long, val amount: BigDecimal)
data class User(val userId: Int, val name:String, val email: String)

object Utils {
    fun sayHello(name: String) {
        println("Hello $name")
    }

    fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,}$")
        return regex.matches(password)
    }

    fun validateEmailWithDomain(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return regex.matches(email)
    }

    fun groupByParity(integerList: List<Int>): Map<String, List<Int>> {
        return integerList.groupBy {
            if(it % 2 == 0) "Pares" else "Impares" }
    }

    fun groupTransactionsValue(transactionList: List<Transaction>): Map<Long, BigDecimal> {
        return transactionList
            .groupBy { it.clientId }
            .mapValues { transactionsByClient ->
                transactionsByClient.value.sumOf { it.amount }
            }
    }

    fun groupTransactionsValueWithFold(transactionList: List<Transaction>): Map<Long, BigDecimal> {
        return transactionList
            .groupingBy { it.clientId }
            .fold(BigDecimal.ZERO) { sumOfTransaction, transaction ->
                sumOfTransaction + transaction.amount
            }
    }

    fun filterList(integerList: List<Int>): List<Int> {
        return integerList.filter { it % 2 == 0 }.sortedDescending()
    }

    fun updateEmail(user: User, newEmail: String): User {
        return user.copy(email = newEmail)
    }

    fun findGreaterElement(integerList: List<Int>): Int {
        if(integerList.size<2){
            throw Exception("Invalid list Size")
        }

        var output = integerList[0]

        for(i in integerList.indices){
           if(output<integerList[i]&&output>integerList[i-1])
               output = integerList[i]
        }

        return output
    }

    fun findSecondGreaterElement(integerList: List<Int>): Int {
        if(integerList.size<2){
            throw Exception("Invalid list Size")
        }

        var greaterElement = integerList[0]
        var secondGreaterElement: Int? = null

        for(i in integerList.indices){
            if(integerList[i]>greaterElement) {
                secondGreaterElement = greaterElement
                greaterElement = integerList[i]
            } else if(greaterElement!=integerList[i]&&(secondGreaterElement==null||integerList[i]>secondGreaterElement)) {
                secondGreaterElement = integerList[i]
            }
        }

        return secondGreaterElement ?: throw Exception("Unable to find the second greater element")
    }

    fun isValidParenthesis(stringToValidate: String): Boolean {
        val stack = ArrayDeque<Char>()

        for(character in stringToValidate){
            if(character in ("([{"))
                stack.add(character)
            else {
                if(stack.isEmpty())
                    return false
                else {
                    if (validateExpression(character, stack)) return false

                    stack.removeLast()
                }
            }
        }

        return stack.isEmpty()
    }

    private fun validateExpression(character: Char, stack: ArrayDeque<Char>): Boolean {
        return (character == ')' && stack.last() != '(') ||
                (character == ']' && stack.last() != '[') ||
                (character == '}' && stack.last() != '{')
    }

    fun findTwoSum(nums: Array<Int>, target: Int): Pair<Int,Int> {
        val numbersToTarget = mutableSetOf<Int>()

        for(number in nums) {
            if(numbersToTarget.contains(target - number))
                return Pair(number, target - number)

            numbersToTarget.add(number)
        }

        return Pair(-1,-1)
    }

    fun binarySearch(nums: Array<Int>, target: Int): Int {
        var left = 0
        var right = nums.size - 1

        while(left <= right) {
            val mid = (left + right) / 2

            if (nums[mid]==target)
                return mid
            else if (nums[mid] < target)
                left += 1
            else
                right -= 1
        }

        return -1
    }
}

fun main() {
    Utils.sayHello("Mailson")
    println("password: 123456 is valid or no? ${Utils.isValidPassword("12345")}")

    println()
    println()

    val transactions = listOf(
        Transaction(1, BigDecimal("100.50")),
        Transaction(2, BigDecimal("200.00")),
        Transaction(1, BigDecimal("50.25")),
        Transaction(2, BigDecimal("75.75")),
        Transaction(3, BigDecimal("300.00"))
    )
    println(transactions)
    println("sumOfTransactionsByClient: ${Utils.groupTransactionsValue(transactions)}")
    println("sumOfTransactionsByClientWithFold: ${Utils.groupTransactionsValueWithFold(transactions)}")

    println()
    println()

    var array = arrayOf(5,4,8,6,98,45,56,78,54,115,425,223,474,102,302)
    var list = arrayListOf<Int>(5,4,8,6,98,45,56,78,54,115,425,223,474,102,302)
    println("List: $list , only pairs, sorted: ${Utils.filterList(list)}")
    list = arrayListOf()
    println("List: $list , only pairs, sorted: ${Utils.filterList(list)}")

    println()
    println()

    val user = User(1, "Mailson", "mailsonfp.dev@gmail.com")
    val newEmail = "mfpereira.2022@outlook.com"
    println("User: $user, newEmail: $newEmail")
    println("User with new email: ${Utils.updateEmail(user,newEmail)}")
    println("is $newEmail a valid email with domain? ${Utils.validateEmailWithDomain(newEmail)}")
    println("is ${newEmail.substring(0,22)} a valid email with domain? ${Utils.validateEmailWithDomain(newEmail.substring(0,22))}")

    println()
    println()

    list = arrayListOf<Int>(735,5,-3,-8,4,6,98,-725,45,56,78,54,-56, 115,425,223,474,102,302)
    println("In list:$list the greater element is: ${Utils.findGreaterElement(list)}")
    println("In list:$list the second greater element is: ${Utils.findSecondGreaterElement(list)}")
    list = arrayListOf(-50, -20, -30, -40, -10)
    println("In list:$list the greater element is: ${Utils.findGreaterElement(list)}")
    println("In list:$list the second greater element is: ${Utils.findSecondGreaterElement(list)}")

    println()
    println()
    var stringToValidate = "([)]"
    println("String:$stringToValidate is a valid expression? ${Utils.isValidParenthesis(stringToValidate)}")
    stringToValidate = "()[]{}"
    println("String:$stringToValidate is a valid expression? ${Utils.isValidParenthesis(stringToValidate)}")

    println()
    println()
    var numsArray = arrayOf(-3, 4, 3, 90)
    println(numsArray.size)
    var target = 0
    println("In array: ${numsArray.toList()}, to find target: $target, you must sum indexes: ${Utils.findTwoSum(numsArray, target)}")
    target = 75
    println("In array: ${numsArray.toList()}, to find target: $target, you must sum indexes: ${Utils.findTwoSum(numsArray, target)}")
    numsArray = arrayOf(2, 7, 11, 15)
    target = 9
    println("In array: ${numsArray.toList()}, to find target: $target, you must sum indexes: ${Utils.findTwoSum(numsArray, target)}")

    println()
    println()
    list = arrayListOf<Int>(735,5,-3,-8,4,6,98,-725,45,56,78,54,-56, 115,425,223,474,102,302)
    println("list:$list grouped by parity: ${Utils.groupByParity(list)}")

    println()
    println()
    numsArray = arrayOf<Int>(735,5,-3,-8,4,6,98,-725,45,56,78,54,-56, 115,425,223,474,102,302)
    numsArray.sort()
    target = 54
    println("In array:${numsArray.toList()} the target: $target is located at index: ${Utils.binarySearch(numsArray,target)}")
    target = 83
    println("In array:${numsArray.toList()} the target: $target is located at index: ${Utils.binarySearch(numsArray,target)}")
}