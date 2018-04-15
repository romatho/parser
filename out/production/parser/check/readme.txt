
J'étais parti sur une classe par passage comme Gauthier avait fait, l'idée était d'avoir à coté de ces passages une structure "tree" contenant les données récupérée qu'i complétait petit à petit.

En cours d'impélmentation j'ai changé d'avis parce que son système est bizarre et pas forcément nécessaire pour les premier passage..
(pour la fin j'en sais rien mais il sera toujours temps de faire des plus petits arbre si c'est nécessaire)
Donc on va utiliser la classe Checker de Philippe.

Pour l'enregistrement de données, j'ai utilisé des hashmaps.

-   il y  une hashmap des classe du program => allowedClass. elle mappe le nom des class autorisée avec les objects correspondants (venant de l'object 'program').
    La Hashmap ne contient que les Class qui sont correcte => les classes qui ne s'appelle pas 'IO' ni 'Object', qui ont un ancêtre correctement défini (donc pas celle qui ont des cycle dans leur ancêtres)

-   Ensuite c'est là que ça devient le souk, j'ai fait une Hashmap de méthod pour chaque classe, le tout dans une hashmap.
    Pourquoi est ce que je fais ça ? ça servira plus tard pour quand on fera maClass.methodDeMaClass(formal1, formal2,...). on pourra récupérer toutes les méthodes de maclass juste en allant rechercher la hashMap associées au nom "maClass"
    Les méthodes qui sont référencées sont les méthodes "correctes" => les méthode qui ne s'appelle pas "main" (sauf si elle appartient à la classe Main) avec un type de retour correct, avec des formals ayant un type correct.
    Un type correct, c'est : int32, bool, string, unit, IO, Object ou bien une classe correcte.
    Donc si un appel est fait a une méthode qui n'existe pas ou qui n'est pas correct on balancera une erreur

-   Finalmement (là c'est inception, mais on en aura p'tet besoin...) j'ai fait une hashtable pour les formals des méthodes correctement définie
    => une hashtable dans une hashtable dans une hastable :)
    ça permettra de vérifier dans les call que les argument envoyé sont les bon.. Il y a probablement moyen de simplement passer par la hastable des méthodes,
    mais bon tant que j'étais lancé j'ai fait ça en plus (comme ça on travaille que avec des hastable au lieu de switcher sur des arraylist pour les formals) au pire on l'utilise pas et on le virera


Récapitulatif:
    -   Les classes mal définie ne sont pas sauvegardée
    -   Dans les classe bien définie, les méthode mal définie ne sont pas gardée
    -   Les formals correspondant à des méthode correcte sont stocké dans des hashtable pour que les données soient homogènes


Ce qu'il faut faire:
    - tester que les hashtables s'initialisent bien et que les erreurs sont bien gérées (je suis dessus mais si vous lisez cette ligne c'est que j'ai pas fini)
    - Gérer les erreurs que j'ai oublié dans les deux premiers passages
    - Créer une fonction de renvoi d'erreur propre et remplacer dans les méthodes public (pour le moment les erreurs sont simplement affichées dans la console)
    - Implémenter le dernier passage qui fait la vérification de la concordance de tout les types => qui ira aussi voir que le type renvoyé par les méthodes est celui attendu, car ça j'ai pas su le checker pour le moment.
    - modifier la classe Expression pour ajouter le type de chaque expression.
    - modifier la méthode toString de Expression pour qu'elle affiche le type de l'expression si il est défini => pas de modification de l'output du parser mais affichage des type quand onn passe dans le checker
    - corriger le parser pour avoir 50/50 au test







