# Rapport Laboratoire 2 SYM
## Henrik Akesson

#### Authentification

> Si une authentification par le serveur est requise, peut-on utiliser un protocole asynchrone ? Quelles seraient les restrictions ? Peut-on utiliser une transmission différée ?

On peut utiliser un protocole asynchrone en le rendant synchrone ou en faisant attention à l'ordre d'envoi des messages.

Certains protocoles d'authentification sont complètement stateless (comme OAuth ou les Json Web Token). Cela permet de faciliter l'authentification asychrone car on n'a pas besoin de la réponse précédente pour envoyer le token. Le seul moment à attendre est la récéption du token.

#### Threads concurrents

> Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se préoccupent de l'envoi et de la réception. Quels problèmes cela peut-il poser ?

Il peut y avoir des problèmes de concurrence. Si le thread  reçoit une information alors que l'autre thread demande cette même information au serveur cela créer des requêtes inutiles.

De même, si le serveur répond lentement ou plus, il pourra y avoir une accumulation de threads en attente.

Il faudrait donc que les threads soient synchronisés entre l'envoi et la réception de paquets.

#### Ecriture différée

##### Transmission différée

L'avantage de cette solution est la simplicité de sa mise en place car il faut simplement garder les transmissions à faire et les différer en cas de problème de communication ou de besoin.

Le désavantage est qu'il peut y avoir beaucoup de transmission à faire et cela pourrait saturer momentanément le reseau.

##### Multiplexer toutes les connexions vers un même serveur en une seule connexion de transport

L'avantage du multiplexage est d'optimiser la bande passante en n'envoyant qu'une seule fois le tout et ainsi éviter de faire de multiples requêtes pour au final le même contenu.

En revanche, cela complexifie la gestion des messages car il faut multiplexer les transmissions dans le bon ordre et faire attention à bien prendre le message le plus récent s'il s'agit du même message.

#### Transmission d’objets

L'inconvénient de JSON qui n'offre pas de service de validation est que cela le rend moins solide face à une application lourde car on ne peut pas contrôler la validité du document.

En revanche, JSON est moins verbeux qu'une infrastructure basé sur XML. Il y a donc moins de données à transferer ce qui le rend très pratique pour des APIs relativement simples.

JSON à aussi l'avantage d'avoir une prise en charge native et/ou simple dans beaucoup d'environements ce qui le rend favorable par rapport à XML.

#### Transmission compressée

Cela dépend du texte transmis et de la quantitée mais pour notre application, on peut constater une amélioration d'environ d'environ 30%.