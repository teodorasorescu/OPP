# Sheriff-of-Nottingham
Sheriff of Nottingham 
	PROGRAMARE ORIENTATA PE OBIECTE
	Sheriff of Nottingham
 NUME: Sorescu Teodora


Pachetul Comparators:
	Acesta contine clase cu rol de comparatori pentru id, profit si map-uri,
 acestia fiind folositi pentru sortarea descrescatoare.
 Clasa Players:
 copyForIllegals:
	-copiaza cartile ilegale intr-o alta lista.
 addonStand:
	-adauga cartile jucatorului respectiv pe taraba.
 sortEquals:
	-sorteaza lista de carti din bag, deoarece cartile cu acelasi profit,
 nu erau puse in ordine dupa acelasi id, ci erau amestecate.

 Clasa CreateTheBag:
 - aceasta apeleaza metoda corespunzatoare comportamentului jucatorului, 
 conform strategiei pe care o abordeaza, in privinta crearii sacului.
 Clasa CreateTheBagStrategies:
 - reprezinta comportamentul fiecarui jucator cand acesta isi formeaza sacul.
 Basic:
 - am folosit un semnalizator, care imi arata daca pachetul de carti contine
 doar bunuri ilegale sau si bunuri legale si ilegale. Daca acesta contine doar
 ilegale, pachetul este sortat dupa profit descrescator,este adaugata doar
 prima carte din acesta, iar apoi este eliminata din pachet. Daca acesta
 contine bunuri amestecate, le alege doar pe cele legale. Pentru sortarea dupa
 frecventa, este folosit un map de frecvente si este calculata valoarea maxima
 si este retinuta cheia acesteia, fiind dupa adaugata in sac. Daca exista in 
 sac mai mult de o valoare, inseamna ca sunt doua bunuri cu aceeasi frecventa
 si urmeaza a fi comparate in functie de profit, analog in functie de id. La
 sfarsit, accesam in map frecventa bunului ramas in sac si daca este mai mare
 decat 1, sunt adaugate atatea bunuri minus unul in sac.
 Greedy:
  - apeleaza metoda basic in rundele impare;
  - Am folosit metoda copyForIllegals, deoarece in cazul in care numarul 
 rundei era par si apeland basic care sterge din sac bunul
 cu profitul maxim (daca pachetul contine doar ilegale) era nevoie sa aleg
 inca o data bunul de profit maxim, dar diferit de cel ales deja.
 Bribed:
 -Este calculat numarul de carti ilegale din pachet si sunt calculate 
 conform acestuia cate carti legale sau ilegale isi permite.
 Daca isi permite mai multe ilegale decat are, sunt adaugate numarul de 
 carti ilegale avut, daca sunt mai putine se compleateza conform cerintei 
 cu cele legale.
 Clasa Inspection:
 - aceasta, conform strategiei adoptate de catre serif, trimite catre 
 metoda corespunzatoare din clasa Stages.
 - In cazul strategiei greedy, cum in aceasta seriful accepta mita, dar mita
 este oferita doar de catre bribed, cand ii verifica pe ceilalti jucatori 
 basic sau greedy, apeleaza verifyGoodsBasic, altfel pentru bribed  
 apeleaza greedyVerifiesBribed. In cazul stategiei bribed, cum acesta ii 
 controleaza doar pe jucatorii din stanga si dreapta sa, am presupus ca este
 asemanator cu asezarea lor in cerc si s-a pus problema daca seriful se afla
 pe prima pozitie si pe ultima in cazul existentei a doi comercianti
 sau a trei. 
 Clasa Stages:
 verifyGoodsBasic:
  - metoda ce reprezinta inspectia propriu-zisa, in aceasta fiind adaugat
 penalty in functie de caz;
  - este folosita in cazul serifilor care nu iau mita;
  - foloseste un semnalizator care arata daca exista ilegale in sac.
 Daca are cel putin un ilegal (carti amestecate), adauga totusi legalele
 in sac daca sunt bine declarate.
 greedyVerifiesBribed:
 - metoda ce reprezinta inspectia propriu-zisa in cazul serifilor care iau
 mita de la jucatori, si anume serifii greedy;
 - stabileste mita si o adauga serifului, respectiv o scade jucatorului.

 Clasa Main:
 scoreCalculator: 
  -calculeaza scorul si il adauga jucatorului.
  -In cazul bonusului bunurilor ilegale, cartile legale care apartin de 
 obtinerea acestuia, sunt adaugate in sac, iar apoi este atribuit fiecarui
 jucator profitul.
 finalBonus:
 - se ocupa cu acordarea lui bonusului King si Queen;
 - am folosit un vector de map-uri, de lungimea numarului de jucatori. Fiecarui
 jucator ii este calculat un map de frecvente. In cazul jucatorilor care nu au
 nimic pe taraba, este creat un map de frecventa initializat la 0.
 - Se itereaza prin fiecare bun legal si este calculata valoarea maxima,
 iterand prin vectorul de map-uri, ce apartine de cheia bunului legal 
 respectiv, si acestuia i se va atribui bonusul King. In privinta bonusului
 Queen,se itereaza printre bunuri si se retine ultima valoare maxima, dar
 mai mica decat maximul de la bonusul King.
 sortMap: 
 - este folosita pentru afisarea clasamentului, sortand map-ul de jucatori.
 main: 
 - se initializeaza jucatorii. Setez fiecare jucator comerciant la inceput, 
 iar apoi la fiecare subrunda sealtereaza setarea jucatorului respectiv serif,
 si resetarea acestuia comerciant.
 - contine jocul propriu-zis, iterandu-se printre runde si subrunde si
 apeland etapele jocului.
