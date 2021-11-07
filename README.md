# TD3

* Tristan Coignion
* Anthony Hanson

## Conception du projet

Le projet est lancé à partir de la classe Debugger, qui va utiliser la CliCommander qui utilisera des commandes.

Le debugee est MyMain.

## Remarques sur certaines commandes

### Sur les breakpoints

#### `break` 

break utilise simplement l'API jdi pour créer une breakpoint request à un endroit spécifique. Si la ligne ou la classe n'existe pas, l'utilisateur est prévenu

#### `breakOnce` et `breakOnCount`

Pour ceux là, on a réutilisé le code de `break` par héritage et on modifié le code de `break` pour qu'il prenne en paramètre le nombre de passages du breakpoint (`break` fait -1 passages, pour dire qu'il ne s'enlève jamais).

Mais `jdi` ne supporte pas le nombre de passage sur les breakpoints. On a créé des `BreakpointReference` stockés dans une liste statique du `Debugger`. Ainsi lorsqu'on retombe sur le breakpoint dans le debugger, on regarde dans la liste combien de passages il lui reste, et s'il ne lui en reste plus, on le supprime de la liste.

#### `breakBeforeMethodCall`

Ce breakpoint-ci utilise les `MethodEntryRequest` de `jdi` et aussi notre propre liste de références. La méthode entry request ne peut filtrer que jusque dans une classe. Cela signifie qu'un break est déclenché pour toutes les entrées de méthodes de la classe. 
En rajoutant notre propre liste de références, nous pouvons filtrer à la main les break requests qui ne sont pas en rapport avec la bonne méthode.

Notons d'ailleurs que le polymorphisme n'est pas vraiment prit en compte ici. C'est à dire que si on fait un break pour une méthode, il fonctionnera pour toutes les méthodes du même nom dans la même classe.

### Commandes d'impression de variables

#### `arguments` `printVar` `temporaries` `receiverVariables`

Ces commandes fonctionnent toutes de la même manière, avec des variations entre elles : On récupère la liste des références des variables (`Field` `LocalVariable`) depuis la frame, ou depuis la définition de la méthode/classe, puis on récupère leur valeur dynamiquement depuis la frame ou l'objet.

Dans le cas de `receiverVariables`, on doit faire attention à faire la différence entre les champs statiques ou non, car on ne récupère pas leurs valeurs de la même manière.

### Commandes d'impression de contexte

#### `method` `receiver` `sender` `stack` `frame`

Ces méthodes manipulent simplement la frame, et les locations pour donner des informations à l'utilisateur sur où il se trouve

### Autres commandes

#### `continue`

Cette commande ne fait absolument rien.

### Commandes non implémentées

#### `stackTop`

Nous aurions pu implémenter cette commande, mais ça aurait signifié devoir tracker chaque return et chaque valeur renvoyée par un return, de manière récursive. Et ça aurait été ridiculeusement difficile. De plus `jdi` ne semble pas apporter de manière simple  de faire cela non plus.

