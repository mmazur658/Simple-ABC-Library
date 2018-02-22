Simple ABC Library – prosta aplikacja webowa java z wykorzystaniem maven, spring i hibernate do zarz¹dzania 
bibliotek¹ podzielona na 3 sekcje: Dla klienta, pracownika i administratora. Klient mo¿e przegl¹daæ ksiêgozbiory i 
rezerwowaæ ksi¹¿ki, edytowaæ swoje dane, zmieniæ has³o i korzystaæ z komunikatora. Pracownik dodatkowo mo¿e dodawaæ, 
usuwaæ i edytowaæ dane ksi¹¿ki, wydawaæ i przyjmowaæ ksi¹¿ki, zarz¹dzaæ rezerwacjami i u¿ytkownikami, przegl¹daæ 
historiê ksi¹¿ki i u¿ytkownika. Administrator dodatkowo mo¿e zwiêkszaæ lub zmniejszaæ uprawnienia u¿ytkownika, przegl¹daæ 
b³êdy i konfiguracjê. Styl CSS ze pobrany ze strony https://codepen.io , wprowadzone zosta³y w³asne zmiany. 

Do zrobienia:
- Spring AOP – after throwing – Po wyst¹pieniu b³êdu wylogowaæ u¿ytkownika i daæ komunikat o b³êdzie, 
zapisaæ stacktrace do text file i zapisaæ plik w bazie danych. Do wyœwietlenia  w sekcji admin- b³êdy. 
- Spring AOP  - after – Po ka¿dej akcji na ksi¹¿ce lub u¿ytkowniku zapisaæ szczegó³y tej akcji w bazie danych. 
Historia ksi¹¿ki/u¿ytkownika do wyœwietlenia w sekcji szczegó³y ksi¹¿ki/u¿ytkownika .
- Przenieœæ opisy wiadomoœci , konfiguracjê rezerwacji i wydawania ksi¹¿ek do properties file. Szczegó³y konfiguracji 
wyœwietliæ w sekcji admin/konfiguracja. 
- Uporz¹dkowanie plików CSS.
- Dodanie opcji: Zamykanie konta u¿ytkownika przez u¿ytkownika lub pracownika. 