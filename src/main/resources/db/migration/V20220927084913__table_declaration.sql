CREATE SEQUENCE cart_id_generator;

CREATE TABLE cart(
	id INTEGER DEFAULT cart_id_generator.nextval,
	client_id INTEGER NOT NULL,
	trans_date DATE NOT NULL,
	done NUMBER (1) CHECK(done = 0 OR done = 1),
	PRIMARY KEY (id),
	FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE SEQUENCE cart_item_id_generator;

CREATE TABLE cart_item(
	id INTEGER DEFAULT cart_item_id_generator.nextval,
	cart_id INTEGER NOT NULL,
	book_id CHAR (13) NOT NULL,
	quantity INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (book_id) REFERENCES book(isbn),
	FOREIGN KEY (cart_id) REFERENCES cart(id)
);