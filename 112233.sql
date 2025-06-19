--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-06-19 08:49:15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 222 (class 1259 OID 24719)
-- Name: cart; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart (
    cart_id integer NOT NULL,
    session_id character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.cart OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 24718)
-- Name: cart_cart_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cart_cart_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cart_cart_id_seq OWNER TO postgres;

--
-- TOC entry 4897 (class 0 OID 0)
-- Dependencies: 221
-- Name: cart_cart_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cart_cart_id_seq OWNED BY public.cart.cart_id;


--
-- TOC entry 230 (class 1259 OID 24813)
-- Name: cart_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cart_items (
    cart_item_id integer NOT NULL,
    cart_id integer NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL
);


ALTER TABLE public.cart_items OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 24812)
-- Name: cart_items_cart_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cart_items_cart_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cart_items_cart_item_id_seq OWNER TO postgres;

--
-- TOC entry 4898 (class 0 OID 0)
-- Dependencies: 229
-- Name: cart_items_cart_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cart_items_cart_item_id_seq OWNED BY public.cart_items.cart_item_id;


--
-- TOC entry 228 (class 1259 OID 24796)
-- Name: order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_items (
    order_item_id integer NOT NULL,
    order_id integer NOT NULL,
    product_id integer NOT NULL,
    quantity integer NOT NULL,
    price numeric(10,2) NOT NULL
);


ALTER TABLE public.order_items OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 24795)
-- Name: order_items_order_item_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_items_order_item_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.order_items_order_item_id_seq OWNER TO postgres;

--
-- TOC entry 4899 (class 0 OID 0)
-- Dependencies: 227
-- Name: order_items_order_item_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_items_order_item_id_seq OWNED BY public.order_items.order_item_id;


--
-- TOC entry 220 (class 1259 OID 24685)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    order_id integer NOT NULL,
    user_id integer,
    recipient_name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    phone_number character varying(20) NOT NULL,
    delivery_address character varying(255) NOT NULL,
    province_city character varying(100) NOT NULL,
    delivery_fee numeric(10,2) NOT NULL,
    is_rush_delivery boolean DEFAULT false,
    rush_delivery_time timestamp without time zone,
    total_amount numeric(10,2) NOT NULL,
    status character varying(20) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT orders_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying, 'CANCELED'::character varying])::text[])))
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 24684)
-- Name: orders_order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_order_id_seq OWNER TO postgres;

--
-- TOC entry 4900 (class 0 OID 0)
-- Dependencies: 219
-- Name: orders_order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.orders_order_id_seq OWNED BY public.orders.order_id;


--
-- TOC entry 232 (class 1259 OID 24830)
-- Name: product_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_history (
    history_id integer NOT NULL,
    product_id integer NOT NULL,
    operation character varying(20) NOT NULL,
    description text,
    user_id integer NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT product_history_operation_check CHECK (((operation)::text = ANY ((ARRAY['ADD'::character varying, 'EDIT'::character varying, 'DELETE'::character varying])::text[])))
);


ALTER TABLE public.product_history OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 24829)
-- Name: product_history_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_history_history_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_history_history_id_seq OWNER TO postgres;

--
-- TOC entry 4901 (class 0 OID 0)
-- Dependencies: 231
-- Name: product_history_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_history_history_id_seq OWNED BY public.product_history.history_id;


--
-- TOC entry 226 (class 1259 OID 24780)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    product_id integer NOT NULL,
    title character varying(100) NOT NULL,
    category character varying(20) NOT NULL,
    value numeric(10,2) NOT NULL,
    price numeric(10,2) NOT NULL,
    stock_quantity integer NOT NULL,
    is_rush_eligible boolean DEFAULT false,
    weight numeric(5,2) NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    authors character varying(255),
    cover_type character varying(20),
    publisher character varying(100),
    publication_date date,
    num_pages integer,
    book_language character varying(50),
    book_genre character varying(50),
    artists character varying(255),
    record_label character varying(100),
    tracklist text,
    music_genre character varying(50),
    release_date date,
    disc_type character varying(20),
    director character varying(255),
    runtime integer,
    studio character varying(100),
    dvd_language character varying(50),
    subtitles character varying(255),
    available character varying(3) DEFAULT 'yes'::character varying,
    CONSTRAINT products_category_check CHECK (((category)::text = ANY ((ARRAY['BOOK'::character varying, 'CD'::character varying, 'LP_RECORD'::character varying, 'DVD'::character varying])::text[]))),
    CONSTRAINT products_cover_type_check CHECK (((cover_type)::text = ANY ((ARRAY['PAPERBACK'::character varying, 'HARDCOVER'::character varying])::text[]))),
    CONSTRAINT products_disc_type_check CHECK (((disc_type)::text = ANY ((ARRAY['BLU_RAY'::character varying, 'HD_DVD'::character varying])::text[])))
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24779)
-- Name: products_product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.products_product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.products_product_id_seq OWNER TO postgres;

--
-- TOC entry 4902 (class 0 OID 0)
-- Dependencies: 225
-- Name: products_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.products_product_id_seq OWNED BY public.products.product_id;


--
-- TOC entry 224 (class 1259 OID 24746)
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    transaction_id integer NOT NULL,
    order_id integer NOT NULL,
    transaction_vnpay_id character varying(50) NOT NULL,
    amount numeric(10,2) NOT NULL,
    content character varying(255),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24745)
-- Name: transactions_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transactions_transaction_id_seq OWNER TO postgres;

--
-- TOC entry 4903 (class 0 OID 0)
-- Dependencies: 223
-- Name: transactions_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_transaction_id_seq OWNED BY public.transactions.transaction_id;


--
-- TOC entry 218 (class 1259 OID 24661)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    role character varying(20) NOT NULL,
    is_blocked boolean DEFAULT false,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'PRODUCT_MANAGER'::character varying, 'CUSTOMER'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 24660)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 4904 (class 0 OID 0)
-- Dependencies: 217
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 4682 (class 2604 OID 24722)
-- Name: cart cart_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart ALTER COLUMN cart_id SET DEFAULT nextval('public.cart_cart_id_seq'::regclass);


--
-- TOC entry 4692 (class 2604 OID 24816)
-- Name: cart_items cart_item_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items ALTER COLUMN cart_item_id SET DEFAULT nextval('public.cart_items_cart_item_id_seq'::regclass);


--
-- TOC entry 4691 (class 2604 OID 24799)
-- Name: order_items order_item_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items ALTER COLUMN order_item_id SET DEFAULT nextval('public.order_items_order_item_id_seq'::regclass);


--
-- TOC entry 4679 (class 2604 OID 24688)
-- Name: orders order_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders ALTER COLUMN order_id SET DEFAULT nextval('public.orders_order_id_seq'::regclass);


--
-- TOC entry 4693 (class 2604 OID 24833)
-- Name: product_history history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history ALTER COLUMN history_id SET DEFAULT nextval('public.product_history_history_id_seq'::regclass);


--
-- TOC entry 4686 (class 2604 OID 24783)
-- Name: products product_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products ALTER COLUMN product_id SET DEFAULT nextval('public.products_product_id_seq'::regclass);


--
-- TOC entry 4684 (class 2604 OID 24749)
-- Name: transactions transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN transaction_id SET DEFAULT nextval('public.transactions_transaction_id_seq'::regclass);


--
-- TOC entry 4676 (class 2604 OID 24664)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 4881 (class 0 OID 24719)
-- Dependencies: 222
-- Data for Name: cart; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cart (cart_id, session_id, created_at) FROM stdin;
2	session_002	2025-06-10 02:41:23.285334
3	session_003	2025-06-10 02:41:23.285334
4	session_004	2025-06-10 02:41:23.285334
5	session_005	2025-06-10 02:41:23.285334
12	85604069-a9d6-430c-9feb-9cf7c5b7120e	2025-06-10 09:28:19.360769
13	196a56fa-bf1d-4d32-8e45-77d845f768d8	2025-06-10 10:01:32.593429
14	e74f9402-f7c6-47ad-98a6-2eafd5e57745	2025-06-10 10:02:43.60475
1	pm	2025-06-10 02:41:23.285334
15	session_001	2025-06-10 10:06:18.949271
16	5b2f2eff-b4f3-4d04-88ac-16bb08c0efb0	2025-06-10 10:38:12.843297
17	eebf785f-b8ba-41d9-bf39-42fe3f5cb344	2025-06-10 10:42:44.724442
18	3102cf9c-2e5c-47ec-8f3b-2e82db45b3d6	2025-06-10 10:43:06.484249
19	bd7355ec-37c4-4b50-b220-7fbcf9cc5b8f	2025-06-17 03:26:31.429044
20	admin	2025-06-17 03:39:18.33459
21	55d1f22e-af34-4f89-aca9-34a715613a3b	2025-06-17 03:39:50.835255
22	34f0860e-b9e6-4a3d-b421-9cc87edea524	2025-06-17 03:42:43.390045
23	ea66aefd-6997-465b-8b95-e5fc7c9d19a6	2025-06-17 05:29:55.16458
24	98144767-052b-4de1-a274-c883f780b697	2025-06-17 05:32:55.505902
25	a3732042-8666-46a6-833a-184d0b860e19	2025-06-17 05:38:56.547299
26	d050d21c-b092-48cd-b61d-ceb56048fef0	2025-06-17 06:46:45.202128
27	cc85e243-c1f8-4a8a-93e2-658561af4b95	2025-06-17 06:49:01.050994
28	3f663aff-47ce-4e50-9b97-f6bf173069bd	2025-06-17 06:51:30.132544
29	a2c193ca-a14b-4464-9cbb-e532fd838393	2025-06-17 06:56:34.314251
30	95919b12-8826-49dd-a440-4941fa39c484	2025-06-17 07:15:31.134861
31	2335a54b-4192-49a3-803a-0bb13bad34bc	2025-06-17 07:18:08.322022
32	19fcd645-327c-4534-b320-63c4aa76c243	2025-06-17 07:21:15.325807
33	8b26df35-f5c7-436e-b109-f8bcd3bb1820	2025-06-17 07:22:55.276019
34	3d10b4f7-63ff-490d-90e3-571a01ba1ba5	2025-06-17 07:52:14.660968
35	4dcc3560-65f8-4982-a411-89b021e4ea54	2025-06-17 07:56:15.792344
36	user	2025-06-17 07:59:23.750556
37	06c63819-a5a4-4f5f-95da-761737af6829	2025-06-17 08:05:52.036187
38	eb9bcffa-4fd8-43af-8c73-b5ea2e1935b4	2025-06-17 08:07:44.420418
41	47b4cfa6-ad78-4ce7-838e-d6b5d211f18d	2025-06-17 09:41:41.248804
43	72609713-0995-439e-92a2-db2b3a58a782	2025-06-17 09:50:07.580786
45	user2	2025-06-17 17:47:03.057944
49	32bff49b-b270-49be-b86e-65b16db8e9da	2025-06-18 08:48:06.556799
50	2ba970ca-4332-4af2-9072-44ce591bc042	2025-06-18 08:58:52.490111
\.


--
-- TOC entry 4889 (class 0 OID 24813)
-- Dependencies: 230
-- Data for Name: cart_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cart_items (cart_item_id, cart_id, product_id, quantity) FROM stdin;
1	1	1	2
2	1	5	1
3	2	3	3
4	2	9	1
5	3	7	2
6	4	2	1
7	4	10	2
8	5	6	1
9	5	12	1
10	28	15	1
11	28	7	6
17	29	11	3
20	29	7	1
21	30	13	1
23	33	9	1
22	33	7	2
25	34	11	1
26	34	9	2
28	34	7	1
30	34	2	1
31	34	4	1
32	34	3	1
33	34	8	1
34	34	14	1
35	34	5	1
36	34	10	1
37	34	6	1
29	34	12	2
39	34	15	1
40	35	13	1
41	35	11	1
45	36	2	1
46	38	9	1
47	38	11	1
48	38	13	1
51	36	7	1
52	36	16	1
53	41	7	1
54	41	9	1
55	41	11	1
56	41	13	1
62	36	6	1
63	36	4	1
64	36	14	1
65	36	8	1
66	36	5	1
42	36	9	21
74	36	11	1
75	36	13	1
76	36	1	1
77	36	15	2
78	1	7	1
\.


--
-- TOC entry 4887 (class 0 OID 24796)
-- Dependencies: 228
-- Data for Name: order_items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_items (order_item_id, order_id, product_id, quantity, price) FROM stdin;
1	1	1	2	100000.00
2	1	5	1	130000.00
3	2	9	1	140000.00
4	2	7	2	160000.00
5	3	3	2	110000.00
6	4	2	1	95000.00
7	4	10	2	135000.00
8	5	6	1	125000.00
9	5	12	1	150000.00
\.


--
-- TOC entry 4879 (class 0 OID 24685)
-- Dependencies: 220
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (order_id, user_id, recipient_name, email, phone_number, delivery_address, province_city, delivery_fee, is_rush_delivery, rush_delivery_time, total_amount, status, created_at) FROM stdin;
1	\N	Nguyen Van A	nguyen.a@gmail.com	0901234567	123 Tran Hung Dao	Hanoi	22000.00	f	\N	245000.00	PENDING	2025-06-10 02:41:23.285334
2	5	Tran Thi B	tran.b@gmail.com	0912345678	456 Le Loi	Ho Chi Minh City	22000.00	t	2025-06-10 04:41:23.285334	320000.00	APPROVED	2025-06-10 02:41:23.285334
3	\N	Le Van C	le.c@gmail.com	0923456789	789 Nguyen Trai	Da Nang	30000.00	f	\N	190000.00	REJECTED	2025-06-10 02:41:23.285334
4	7	Pham Thi D	pham.d@gmail.com	0934567890	101 Hai Ba Trung	Hanoi	32000.00	t	2025-06-10 04:41:23.285334	450000.00	PENDING	2025-06-10 02:41:23.285334
5	\N	Hoang Van E	hoang.e@gmail.com	0945678901	202 Vo Van Tan	Can Tho	30000.00	f	\N	135000.00	CANCELED	2025-06-10 02:41:23.285334
\.


--
-- TOC entry 4891 (class 0 OID 24830)
-- Dependencies: 232
-- Data for Name: product_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product_history (history_id, product_id, operation, description, user_id, created_at) FROM stdin;
1	1	ADD	Added new book: The Great Gatsby	3	2025-06-10 02:41:23.285334
2	5	ADD	Added new CD: Abbey Road	3	2025-06-10 02:41:23.285334
3	7	EDIT	Updated stock quantity for Dark Side of the Moon	4	2025-06-10 02:41:23.285334
4	9	ADD	Added new DVD: Inception	3	2025-06-10 02:41:23.285334
5	3	EDIT	Updated price for 1984	4	2025-06-10 02:41:23.285334
6	2	ADD	Added new book: Pride and Prejudice	3	2025-06-10 02:41:23.285334
7	10	DELETE	Removed The Matrix due to discontinuation	4	2025-06-10 02:41:23.285334
8	6	ADD	Added new CD: Thriller	3	2025-06-10 02:41:23.285334
9	12	ADD	Added new DVD: Spirited Away	3	2025-06-10 02:41:23.285334
10	1	EDIT	Updated publisher for The Great Gatsby	4	2025-06-10 02:41:23.285334
11	7	EDIT	Updated product: 1984	12	2025-06-10 10:04:57.72268
12	7	EDIT	Updated product: 1984	12	2025-06-10 10:05:13.490005
\.


--
-- TOC entry 4885 (class 0 OID 24780)
-- Dependencies: 226
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (product_id, title, category, value, price, stock_quantity, is_rush_eligible, weight, created_at, updated_at, authors, cover_type, publisher, publication_date, num_pages, book_language, book_genre, artists, record_label, tracklist, music_genre, release_date, disc_type, director, runtime, studio, dvd_language, subtitles, available) FROM stdin;
7	1984	BOOK	100000.00	110000.00	40	t	0.40	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	George Orwell	PAPERBACK	Secker & Warburg	1949-06-08	328	English	Dystopian	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	yes
8	To Kill a Mockingbird	BOOK	85000.00	90000.00	60	t	0.50	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	Harper Lee	HARDCOVER	J.B. Lippincott	1960-07-11	281	English	Fiction	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	yes
9	Abbey Road	CD	120000.00	130000.00	25	f	0.20	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	The Beatles	Apple Records	Come Together, Something, Octopus's Garden	Rock	1969-09-26	\N	\N	\N	\N	\N	\N	yes
10	Thriller	CD	110000.00	125000.00	35	t	0.20	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	Michael Jackson	Epic Records	Billie Jean, Beat It, Thriller	Pop	1982-11-30	\N	\N	\N	\N	\N	\N	yes
11	Dark Side of the Moon	LP_RECORD	150000.00	160000.00	20	t	0.40	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	Pink Floyd	Harvest Records	Time, Money, Us and Them	Rock	1973-03-01	\N	\N	\N	\N	\N	\N	yes
12	Led Zeppelin IV	LP_RECORD	140000.00	155000.00	15	f	0.40	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	Led Zeppelin	Atlantic Records	Stairway to Heaven, Black Dog	Rock	1971-11-08	\N	\N	\N	\N	\N	\N	yes
13	Inception	DVD	130000.00	140000.00	30	t	0.30	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	BLU_RAY	Christopher Nolan	148	Warner Bros	English	English, Spanish	yes
14	The Matrix	DVD	120000.00	135000.00	40	t	0.30	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	BLU_RAY	Wachowski Sisters	136	Warner Bros	English	English, French	yes
15	Pulp Fiction	DVD	115000.00	130000.00	25	f	0.30	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	BLU_RAY	Quentin Tarantino	154	Miramax	English	English, Spanish	yes
16	Spirited Away	DVD	140000.00	150000.00	20	t	0.30	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	BLU_RAY	Hayao Miyazaki	125	Studio Ghibli	Japanese	English, Vietnamese	yes
1	Sample Book	BOOK	90000.00	100000.00	50	t	0.50	2025-06-10 02:37:58.947605	2025-06-10 02:37:58.947605	John Doe	HARDCOVER	Sample Publisher	2023-01-15	300	English	Fiction	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	no
2	Sample CD	CD	80000.00	90000.00	30	f	0.20	2025-06-10 02:37:58.947605	2025-06-10 02:37:58.947605	\N	\N	\N	\N	\N	\N	\N	Band XYZ	Sample Label	Track 1, Track 2, Track 3	Pop	2023-06-20	\N	\N	\N	\N	\N	\N	no
3	Sample LP Record	LP_RECORD	120000.00	130000.00	20	t	0.40	2025-06-10 02:37:58.947605	2025-06-10 02:37:58.947605	\N	\N	\N	\N	\N	\N	\N	Band ABC	Classic Label	Track A, Track B	Rock	2023-07-10	\N	\N	\N	\N	\N	\N	no
4	Sample DVD	DVD	150000.00	160000.00	40	t	0.30	2025-06-10 02:37:58.947605	2025-06-10 02:37:58.947605	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	BLU_RAY	Jane Smith	120	Sample Studio	English	English, Spanish	no
5	The Great Gatsby	BOOK	90000.00	100000.00	50	t	0.50	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	F. Scott Fitzgerald	PAPERBACK	Scribner	1925-04-10	180	English	Fiction	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	no
6	Pride and Prejudice	BOOK	80000.00	95000.00	30	f	0.60	2025-06-10 02:41:23.285334	2025-06-10 02:41:23.285334	Jane Austen	HARDCOVER	Penguin Books	1813-01-28	432	English	Romance	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	no
\.


--
-- TOC entry 4883 (class 0 OID 24746)
-- Dependencies: 224
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (transaction_id, order_id, transaction_vnpay_id, amount, content, created_at) FROM stdin;
1	1	VNPAY_001	245000.00	Payment for order 1	2025-06-10 02:41:23.285334
2	2	VNPAY_002	320000.00	Payment for order 2	2025-06-10 02:41:23.285334
3	4	VNPAY_003	450000.00	Payment for order 4	2025-06-10 02:41:23.285334
4	5	VNPAY_004	135000.00	Refund for order 5	2025-06-10 02:41:23.285334
\.


--
-- TOC entry 4877 (class 0 OID 24661)
-- Dependencies: 218
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, username, password, email, role, is_blocked, created_at) FROM stdin;
1	admin1	hashed_password_1	admin1@aims.com	ADMIN	f	2025-06-10 02:41:23.285334
2	admin2	hashed_password_2	admin2@aims.com	ADMIN	f	2025-06-10 02:41:23.285334
3	pm1	hashed_password_3	pm1@aims.com	PRODUCT_MANAGER	f	2025-06-10 02:41:23.285334
4	pm2	hashed_password_4	pm2@aims.com	PRODUCT_MANAGER	f	2025-06-10 02:41:23.285334
5	customer1	hashed_password_5	customer1@aims.com	CUSTOMER	f	2025-06-10 02:41:23.285334
6	customer2	hashed_password_6	customer2@aims.com	CUSTOMER	t	2025-06-10 02:41:23.285334
7	customer3	hashed_password_7	customer3@aims.com	CUSTOMER	f	2025-06-10 02:41:23.285334
8	customer4	hashed_password_8	customer4@aims.com	CUSTOMER	f	2025-06-10 02:41:23.285334
10	testuser	hashed_password_10	test@aims.com	CUSTOMER	f	2025-06-10 02:41:23.285334
12	pm	1	pm@aims.com	PRODUCT_MANAGER	f	2025-06-10 02:44:23.952167
13	user	1	user@aims.com	CUSTOMER	f	2025-06-10 02:44:23.952167
14	user2	111111	user2test@gmail.com	CUSTOMER	f	2025-06-17 17:46:56.263049
11	admin	1	admin@aims.com	ADMIN	f	2025-06-10 02:43:08.979441
15	user1test	default_password	userdddd@gmail.com	ADMIN	f	2025-06-18 15:31:09.002179
9	customer5	hashed_password_9	customer5@aims.com	PRODUCT_MANAGER	t	2025-06-10 02:41:23.285334
16	user10	1	user10@aims.com	CUSTOMER	f	2025-06-18 16:00:15.169227
17	glglglglglgl	111111	glglglgl@gmail.com	ADMIN	f	2025-06-18 23:10:54.61299
\.


--
-- TOC entry 4905 (class 0 OID 0)
-- Dependencies: 221
-- Name: cart_cart_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cart_cart_id_seq', 53, true);


--
-- TOC entry 4906 (class 0 OID 0)
-- Dependencies: 229
-- Name: cart_items_cart_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cart_items_cart_item_id_seq', 78, true);


--
-- TOC entry 4907 (class 0 OID 0)
-- Dependencies: 227
-- Name: order_items_order_item_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_items_order_item_id_seq', 9, true);


--
-- TOC entry 4908 (class 0 OID 0)
-- Dependencies: 219
-- Name: orders_order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_order_id_seq', 5, true);


--
-- TOC entry 4909 (class 0 OID 0)
-- Dependencies: 231
-- Name: product_history_history_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_history_history_id_seq', 16, true);


--
-- TOC entry 4910 (class 0 OID 0)
-- Dependencies: 225
-- Name: products_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_product_id_seq', 16, true);


--
-- TOC entry 4911 (class 0 OID 0)
-- Dependencies: 223
-- Name: transactions_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_transaction_id_seq', 4, true);


--
-- TOC entry 4912 (class 0 OID 0)
-- Dependencies: 217
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 17, true);


--
-- TOC entry 4718 (class 2606 OID 24818)
-- Name: cart_items cart_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (cart_item_id);


--
-- TOC entry 4708 (class 2606 OID 24725)
-- Name: cart cart_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (cart_id);


--
-- TOC entry 4710 (class 2606 OID 24727)
-- Name: cart cart_session_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_session_id_key UNIQUE (session_id);


--
-- TOC entry 4716 (class 2606 OID 24801)
-- Name: order_items order_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (order_item_id);


--
-- TOC entry 4706 (class 2606 OID 24695)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);


--
-- TOC entry 4722 (class 2606 OID 24839)
-- Name: product_history product_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_pkey PRIMARY KEY (history_id);


--
-- TOC entry 4714 (class 2606 OID 24793)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (product_id);


--
-- TOC entry 4712 (class 2606 OID 24752)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id);


--
-- TOC entry 4720 (class 2606 OID 24851)
-- Name: cart_items unique_cart_product; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT unique_cart_product UNIQUE (cart_id, product_id);


--
-- TOC entry 4702 (class 2606 OID 24669)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4704 (class 2606 OID 24671)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4727 (class 2606 OID 24819)
-- Name: cart_items cart_items_cart_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.cart(cart_id);


--
-- TOC entry 4728 (class 2606 OID 24824)
-- Name: cart_items cart_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(product_id);


--
-- TOC entry 4725 (class 2606 OID 24802)
-- Name: order_items order_items_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id);


--
-- TOC entry 4726 (class 2606 OID 24807)
-- Name: order_items order_items_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_items
    ADD CONSTRAINT order_items_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(product_id);


--
-- TOC entry 4723 (class 2606 OID 24696)
-- Name: orders orders_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 4729 (class 2606 OID 24840)
-- Name: product_history product_history_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(product_id);


--
-- TOC entry 4730 (class 2606 OID 24845)
-- Name: product_history product_history_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product_history
    ADD CONSTRAINT product_history_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 4724 (class 2606 OID 24753)
-- Name: transactions transactions_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_order_id_fkey FOREIGN KEY (order_id) REFERENCES public.orders(order_id);


-- Completed on 2025-06-19 08:49:15

--
-- PostgreSQL database dump complete
--

