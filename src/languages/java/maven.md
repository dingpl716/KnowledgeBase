### Maven的总体结构

  - Maven有三个标准的lifecycles
  - 每个lifecycle又有若干个预定义的phase
  - 每个phase都可以有pre和post phase
  - 在每个phase里面又可以执行多个goal

  - clean
  - default (or build)
  - site