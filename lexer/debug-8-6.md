
```
========================================
入力文字列:
// this line is comment

----------------------------------------
Token 1: <COMMENT, tag: 257, lexeme: "// this line is comment"> (Tag: 257)
----------------------------------------
最終行数: 3
テスト終了

========================================
入力文字列:
10 + 20 = 30 //
----------------------------------------
Token 1: <NUM, value: 10> (Tag: 256)
Token 2: <'+'> (Tag: 43)
Token 3: <NUM, value: 20> (Tag: 256)
Token 4: <'='> (Tag: 61)
Token 5: <NUM, value: 30> (Tag: 256)
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        at java.base/java.util.Arrays.copyOf(Arrays.java:3537)
        at java.base/java.lang.AbstractStringBuilder.ensureCapacityNewCoder(AbstractStringBuilder.java:282)
        at java.base/java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:897)
        at java.base/java.lang.StringBuffer.append(StringBuffer.java:422)
        at lexer.Lexer.scan(Lexer.java:44)
        at lexer.Lexer.runTest(Lexer.java:110)
        at lexer.Lexer.main(Lexer.java:161)
```

最小ケース
```
========================================
入力文字列:
// this line is comment

----------------------------------------
Token 1: <COMMENT, tag: 257, lexeme: "// this line is comment"> (Tag: 257)
----------------------------------------
最終行数: 3
テスト終了

========================================
入力文字列:
//
----------------------------------------
^C%
```


最終行数が本来より+1されている場合がある
-> 行コメントの停止条件に EOFが加わったが､EOFの時もlineを+1していた｡
->
```
========================================
入力文字列:
10 + 20 = 30 //meaning less
----------------------------------------
Token 1: <NUM, value: 10> (Tag: 256)
Token 2: <'+'> (Tag: 43)
Token 3: <NUM, value: 20> (Tag: 256)
Token 4: <'='> (Tag: 61)
Token 5: <NUM, value: 30> (Tag: 256)
Token 6: <COMMENT, tag: 257, lexeme: "//meaning less"> (Tag: 257)
----------------------------------------
最終行数: 2
テスト終了

========================================
入力文字列:
10 + 20 = 30 // meaning less
----------------------------------------
Token 1: <NUM, value: 10> (Tag: 256)
Token 2: <'+'> (Tag: 43)
Token 3: <NUM, value: 20> (Tag: 256)
Token 4: <'='> (Tag: 61)
Token 5: <NUM, value: 30> (Tag: 256)
Token 6: <COMMENT, tag: 257, lexeme: "// meaning less"> (Tag: 257)
----------------------------------------
最終行数: 2
テスト終了
```
