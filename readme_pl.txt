Simple ABC Library � prosta aplikacja webowa java z wykorzystaniem maven, spring i hibernate do zarz�dzania 
bibliotek� podzielona na 3 sekcje: Dla klienta, pracownika i administratora. Klient mo�e przegl�da� ksi�gozbiory i 
rezerwowa� ksi��ki, edytowa� swoje dane, zmieni� has�o i korzysta� z komunikatora. Pracownik dodatkowo mo�e dodawa�, 
usuwa� i edytowa� dane ksi��ki, wydawa� i przyjmowa� ksi��ki, zarz�dza� rezerwacjami i u�ytkownikami, przegl�da� 
histori� ksi��ki i u�ytkownika. Administrator dodatkowo mo�e zwi�ksza� lub zmniejsza� uprawnienia u�ytkownika, przegl�da� 
b��dy i konfiguracj�. Styl CSS ze pobrany ze strony https://codepen.io , wprowadzone zosta�y w�asne zmiany. 

Do zrobienia:
- Spring AOP � after throwing � Po wyst�pieniu b��du wylogowa� u�ytkownika i da� komunikat o b��dzie, 
zapisa� stacktrace do text file i zapisa� plik w bazie danych. Do wy�wietlenia  w sekcji admin- b��dy. 
- Spring AOP  - after � Po ka�dej akcji na ksi��ce lub u�ytkowniku zapisa� szczeg�y tej akcji w bazie danych. 
Historia ksi��ki/u�ytkownika do wy�wietlenia w sekcji szczeg�y ksi��ki/u�ytkownika .
- Przenie�� opisy wiadomo�ci , konfiguracj� rezerwacji i wydawania ksi��ek do properties file. Szczeg�y konfiguracji 
wy�wietli� w sekcji admin/konfiguracja. 
- Uporz�dkowanie plik�w CSS.
- Dodanie opcji: Zamykanie konta u�ytkownika przez u�ytkownika lub pracownika. 