Como funciona:
O programa é baseado no Twitter. 
Os utilizadores podem seguir-se uns aos outros e publicar dweeds.
Ao buscar o feed de um utilizador, são apresentadas as publicações dos utilizadores que segue (do mais recente para o mais antigo)
A data de publicação e o nome são adicionados automaticamente aos dweeds.

Comandos:
register(username) - regista o utilizador com nome username, caso não exista
follow(follower, followed) - faz com que o utilizador "follower" siga o "followed", caso existam
unfollow(follower, followed) - faz com que o utilizador "follower" deixe de seguir o "followed", caso existam
dweed(username, text) - publica um dweed do utilizador "username" com o texto "text"
feed(username) - apresenta o feed do utilizador "username"
followers(username) - apresenta os seguidores do utilizador "username"
sleep - pausa o programa por um segundo, utilizado para espaçar no tempo os dweeds no ficheiro de teste example_commands.txt
exit - termina o programa

O programa pode ser corrido com o argumento "flushdb" para limpar a base de dados do redis ao iniciar.

Estruturas usadas no redis:

"userset"
Set que guarda os usernames dos utilizadores registados
Utiliza-se para verificações de existência de utilizadores.

"someuser:flwrs"
Guarda os usernames dos utilizadores que seguem o utilizador com username "someuser"
Criado para o utilizador "someuser" assim que alguém o segue pela primeira vez
Utiliza-se para 

"someuser:flwd"
Guarda os usernames dos utilizadores que o utilizador com username "someuser" segue
Criado para o utilizador "someuser" assim que segue alguém pela primeira vez

"someuser:dweeds"
Guarda os dweeds feitos pelo utilizador "someuser"
Criado para o utilizador "someuser" assim que publica um dweed pela primeira vez