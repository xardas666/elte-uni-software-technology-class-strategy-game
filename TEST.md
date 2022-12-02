#Tesztelés

##Új kör
* játékost vált

##Játékvége
* meghal minden embere és nincs épülete

##Építkezés
* kaszárnya bárhova
* munkahelyek csak az adott resource mellé
* túl messze nem építhet
* csak üres helyre építhet
* csak akkor épít, ha ki tudja fizetni
* építkezés megy körönként
* építés alatt nem használható

##Termelés
* amennyi paraszt annyi * resource
* nincs paraszt nincs termelés
* ha még nincs kész épület, nem termel

##Mozgás
* mozgáspont fogy
* mozgáspont kifogy -> nincs mozgás
* ha már cselekedett nem mozoghat
* akadályra nem lehet lépni
* sárkány tud akadályra lépni

##Karakter készítés
* a karakter elkészül az adott helyen, az adott játékoshoz
* túl messze nem készíthet
* csak üres helyre készíthet
* csak akkor készít, ha ki tudja fizetni

##Támadás
* roll 20 + hit ha nagyobb mint a támadott shield-je akkor sebez
* csak szomszédos játékost támadhat
* nem támadhat saját játékost
* ha már cselekedett nem támadhat

#misc
* junit
* maven build -> full jar futtatható
