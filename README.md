## FATAYERJI Ahmad et HALGAND Kyllian - Mon Bo Tablo

### Compilation

Pour compiler le programme, utiliser la commande suivante depuis le répertoire du projet :

```shell
javac -source 1.7 -target 1.7 -d FatayerjiHalgand/bin FatayerjiHalgand/src/*.java
```
### Execution
```
## Pour Quad Tree
java -classpath FatayerjiHalgand/bin MonBoTablo 1 FatayerjiHalgand/data/input1.txt FatayerjiHalgand/output
## Pour Ternary Tree
java -classpath FatayerjiHalgand/bin MonBoTablo 2 FatayerjiHalgand/data/input2.txt FatayerjiHalgand/output
```


Les 2 manières utilisent le même fichier en entré. Pour le quad tree, on utilise les 4 couleurs de la ligne. Pour le ternary tree, on ignore simplement la dernière valeur. ainsi la ligne :

```
600, 500, R, G, J, B
```

Va crééer un ```QuadNoeud``` aux coordonnées ```600,500```, avec ses 4 fils de couleur ```Rouge, Gris, Jaune et Bleu```.

Pour le ```TerNoeud```, il va créer un noeud aux coordonnées ```600,500```, avec ses 3 fils de couleur ```Rouge, Gris et Jaune```.


## Fonctionnement du Ternary Tree

Pour l'arbre ternaire, nous avons décidé de fusionner la partie Nord Ouest, et la partie Sud Ouest, pour ne faire qu'une seule partie Ouest. Ainsi nous avons 3 régions, dont l'une plus grande que les 2 autres.
