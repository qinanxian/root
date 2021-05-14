getFiscalBook
===
* 获取单个账套对象
```sql
select * from FISC_BOOK where BOOK_CODE=:bookCode
```

getFiscalBookPeriods
===
* 获取账套下的科目列表
```sql
select * from FISC_BOOK_PERIOD 
where BOOK_CODE=:bookCode
order by PERIOD_YEAR ASC,PERIOD_TERM ASC
```

getFiscalBookEntries
===
* 获取账套下的会计期间
```sql
select * 
from FISC_BOOK_ENTRY 
where BOOK_CODE=:bookCode
order by BOOK_CODE ASC, ENTRY_CODE ASC
```

getFiscalBookAssits
===
* 获取账套下的科目的辅助科目
```sql
select * from FISC_BOOK_ASSIST M
where exists(
      select 1 from 
        FISC_BOOK_ENTRY R 
        where M.BOOK_ENTRY_ID=R.BOOK_ENTRY_ID
        and R.BOOK_CODE=:bookCode)
```