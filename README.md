# CMPE327 Banking Simulation Console
# Console to run a simulation of a bank:
# 1) Two users:
    a) agent (has all permissions, transaction limt of $999,999.99)
    b) machine (cannot create or delete accounts and has a limit of $1000.00 for deposits, transfers, and withdrawals)
# 2) Methods:
    a) login: login to console
    b) logout: log out of console
    c) createacct: create an account (includes account number, and name)
    d) deleteacct: delete an account (removing both account number and name)
    e) deposit: deposit money into an account (request for account number and amount to deposit)
    f) transfer: transfer money from one account to another (request account number from bother accounts and amount to transfer)
    g) withdraw: withdraw money from an account (request for account number and amount to withdraw)
# 3) Files:
    a) Valid Accounts
      - records the accounts that are currently available in program
    b) Transaction Summary
      - store a line describing every transaction (createacct, deleteacct, deposit, transfer, withdraw)
      - end file with EOF (end-of-file)
