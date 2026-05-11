package com.pao.laboratory11.exercise2;

public class Transaction {

int id;
double amount;
String date;
String country;
String channel;
String accountId;

public Transaction(int id,
                   double amount,
                   String date,
                   String country,
                   String channel,
                   String accountId) {

    this.id = id;
    this.amount = amount;
    this.date = date;
    this.country = country;
    this.channel = channel;
    this.accountId = accountId;
}
}