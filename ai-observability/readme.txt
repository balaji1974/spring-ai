Just wanted to point out that the property name of credsStore is correct. 
docker login | Docker Documentation. This is usually an error that happens when 
the credsStore value config (in this case “desktop”) cannot be retrieved. 
In my case, the underlying problem was that I installed Docker Desktop but 
then reinstalled it to use just Docker Engine with Desktop. 

The credsStore value in ~/.docker/config.json did not update or remove the credsStore value, 
so it errored properly. If you are not using another credsStore, just remove the value as a whole. 
Renaming it to credStore may just confuse you later if you ever need to look at it again.