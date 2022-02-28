# Kellschain

## Introduction
A blockchain is a digital ledger that records transactions. It is a decentralized, peer-to-peer network of public 
records that can be verified by a group of people.

On a whim I decided to learn about Blockchain and
how it works.
Ended up with a simple proof of concept that I can use to learn more about blockchain.

So,<p>
I Successfully created my own cryptocurrency (sort of!).

---

## Blockchain State now:

- [x] Allows users to create wallets with:

```java
    Wallet myWallet = new Wallet();
```

- [x] Provides wallets with public and private keys using Elliptic-Curve cryptography.
- [x] Secures the transfer of funds, by using a digital signature algorithm to prove ownership. 
- [x] Allows users to make transactions on the blockchain with:

```java
     Block.addTransaction(walletA.sendFunds(myWallet.publicKey, 20));
```

---

### Something to note:
This is just a proof of concept. I am not sure if it is a good idea to use blockchain in a real world application.
At the very minimum without some highly sophisticated security, and an over all optimization to the blockchain engine.

And also if you are looking for Information on Blockchain, there are most definitely better resources to look at.