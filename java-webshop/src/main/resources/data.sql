insert into category (name) values ('Programozás');
insert into category (name) values ('Mesterséges intelligencia');
insert into category (name) values ('Adattudomány');
insert into category (name) values ('Számítógépes hálózatok');
insert into category (name) values ('Adatbázisok');
insert into category (name) values ('Operációs rendszerek');

INSERT INTO users (name, username, password, email, role) VALUES ('Balogh József', 'Jozsika02', 'jelszo', 'bjozsef@gmail.com', 'USER');
INSERT INTO users (name, username, password, email, role) VALUES ('Buzás Ádám', 'adamkaj', 'valami', 'adam2000buzas@gmail.com', 'ADMIN');

INSERT INTO shipping_type (name, price) VALUES ('Standard-Szállítás', 1250);
INSERT INTO shipping_type (name, price) VALUES ('Express-Szállítás', 2000);

INSERT INTO book (title, author, publish_year, price, category_id, img_url, description) VALUES ('Clean Code', 'Robert C. Martin', 2008, 3400, 1, 'https://m.media-amazon.com/images/I/51E2055ZGUL._AC_UF1000,1000_QL80_.jpg', 'Fedezd fel az olyan fejlesztési gyakorlatokat, amelyek révén kódod könnyen karbantartható és érthető lesz a "Clean Code: A Handbook of Agile Software Craftsmanship" című könyvvel, melyet Robert C. Martin írt. Ez a könyv alapvető olvasmány minden fejlesztő számára, aki magas színvonalú, könnyen érthető és karbantartható kódot kíván írni.

A "Clean Code" könyvben Robert C. Martin részletesen bemutatja azokat az elveket és gyakorlatokat, amelyek segítségével a fejlesztők jobb kódokat hozhatnak létre. Megismerheted a tiszta kód alapelveit, a megfelelő elnevezési konvenciókat, az egyszerűség fontosságát és még sok más technikát, amelyek elősegítik a minőségi kód létrehozását.');
INSERT INTO book (title, author, publish_year, price, category_id, img_url, description) VALUES ('Artificial Intelligence: A Modern Approach', 'Stuart Russell és Peter Norvig', 2017, 4100, 2, 'https://m.media-amazon.com/images/I/61nHC3YWZlL._AC_UF1000,1000_QL80_.jpg', '"A Modern Approach to Artificial Intelligence" (Magyar cím: "Mesterséges Intelligencia: Egy Modern Megközelítés") egy könyv, melyet Stuart Russell és Peter Norvig írt. Ez egy alapvető és kiterjedt mű, mely bemutatja a mesterséges intelligencia (MI) különböző területeit és technikáit.

A könyv alapvetően a mesterséges intelligencia területén történt fejlődés legújabb eredményeit és módszereit mutatja be, olyan témákat érintve, mint a problémamegoldás, a tudásszerkezetek, a gépi tanulás, a logikai következtetés és sok más MI terület. Bemutatja az algoritmusokat és azok alkalmazását a MI problémák megoldására.');
INSERT INTO book (title, author, publish_year, price, category_id, img_url, description) VALUES ('Deep Learning', 'Ian Goodfellow, Yoshua Bengio', 2015, 3800, 2, 'https://m.media-amazon.com/images/I/A10G+oKN3LL.jpg', 'Az "Deep Learning" című könyv a mesterséges intelligencia területének egy meghatározó műve, melyet Ian Goodfellow, Yoshua Bengio és Aaron Courville írtak. A könyv alapvetően a mélytanulás (deep learning) területét járja körül, bemutatva a neurális hálók és gépi tanulás alapjait, módszereit és alkalmazásait.

A könyv részletesen ismerteti a mélytanulás elméletét és gyakorlatát, és olyan témákat érint, mint a konvolúciós hálók, rekurrens hálók, gépi látás, természetes nyelvfeldolgozás és sok más terület, ahol a mélytanulás rendkívül hatékonyan alkalmazható.');
INSERT INTO book (title, author, publish_year, price, category_id, img_url, description) VALUES ('Python Data Science Handbook', 'Jake VanderPlas ', 2013, 3200, 3, 'https://m.media-amazon.com/images/I/91Yqv5wWuPL._AC_UF1000,1000_QL80_.jpg', 'A "Python Data Science Handbook" Jake VanderPlas által írt könyv, amely egy kiváló forrás és útmutató a Python programozási nyelv használatához adatai elemzéséhez és tudományos vizsgálatokhoz. A könyv célja, hogy segítsen a szakembereknek és a kezdőknek egyaránt elsajátítani a Python nyelvet és annak alkalmazását a data science területén.

A könyvben átfogóan bemutatja a következőket:

A Python alapjait és adatai elemzéshez szükséges fontos könyvtárakat (pandas, NumPy, matplotlib, scikit-learn stb.).
Az adatfeldolgozás és adatvizualizáció legfontosabb eszközeit és technikáit.
A gépi tanulás és deep learning alapjait, és hogyan lehet ezeket alkalmazni a Pythonban.');
INSERT INTO book (title, author, publish_year, price, category_id, img_url, description) VALUES ('Data Science for Business', 'Foster Provost és Tom Fawcett', 2014, 3600, 3, 'https://m.media-amazon.com/images/I/91lOK2jVqPL._AC_UF1000,1000_QL80_.jpg', 'Fedezd fel az üzleti világban rejlő adatok értékét és hatékony felhasználását "Data Science for Business" című könyvvel, amelyet Foster Provost és Tom Fawcett írt. Ez a könyv alapvető olvasmány mindazok számára, akik értelmezni és kiaknázni szeretnék az adatok erejét üzleti döntéseikben.

A "Data Science for Business" könyvben az írók részletesen bemutatják, hogyan lehet adatokat gyűjteni, elemzni és felhasználni a vállalkozások stratégiai döntéseihez. Megismerheted az adattudomány alapjait, beleértve a gépi tanulás, az adatbányászat és az előrejelzési módszereket. Az üzleti példák és esettanulmányok segítenek abban, hogy lássd, hogyan alkalmazhatod ezeket az elveket a saját vállalkozásodban vagy projektjeidben.');
