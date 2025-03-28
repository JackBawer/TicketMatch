--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

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
-- Name: match; Type: TABLE; Schema: public; Owner: jack
--

CREATE TABLE public.match (
    id_match integer NOT NULL,
    equipe1 character varying(100) NOT NULL,
    equipe2 character varying(100) NOT NULL,
    date_match date NOT NULL,
    heure_match time without time zone NOT NULL,
    lieu character varying(150) NOT NULL,
    capacite_stade integer NOT NULL,
    CONSTRAINT match_capacite_stade_check CHECK ((capacite_stade > 0))
);


ALTER TABLE public.match OWNER TO jack;

--
-- Name: match_id_match_seq; Type: SEQUENCE; Schema: public; Owner: jack
--

CREATE SEQUENCE public.match_id_match_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.match_id_match_seq OWNER TO jack;

--
-- Name: match_id_match_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jack
--

ALTER SEQUENCE public.match_id_match_seq OWNED BY public.match.id_match;


--
-- Name: paiement; Type: TABLE; Schema: public; Owner: jack
--

CREATE TABLE public.paiement (
    id_paiement integer NOT NULL,
    id_ticket integer NOT NULL,
    id_utilisateur integer NOT NULL,
    mode_paiement character varying(50) NOT NULL,
    statut character varying(10) NOT NULL,
    date_paiement timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT paiement_statut_check CHECK (((statut)::text = ANY ((ARRAY['validé'::character varying, 'refusé'::character varying])::text[])))
);


ALTER TABLE public.paiement OWNER TO jack;

--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE; Schema: public; Owner: jack
--

CREATE SEQUENCE public.paiement_id_paiement_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.paiement_id_paiement_seq OWNER TO jack;

--
-- Name: paiement_id_paiement_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jack
--

ALTER SEQUENCE public.paiement_id_paiement_seq OWNED BY public.paiement.id_paiement;


--
-- Name: ticket; Type: TABLE; Schema: public; Owner: jack
--

CREATE TABLE public.ticket (
    id_ticket integer NOT NULL,
    id_utilisateur integer NOT NULL,
    id_match integer NOT NULL,
    prix numeric(10,2) NOT NULL,
    statut character varying(10) NOT NULL,
    numero_place integer NOT NULL,
    CONSTRAINT ticket_numero_place_check CHECK ((numero_place > 0)),
    CONSTRAINT ticket_prix_check CHECK ((prix >= (0)::numeric)),
    CONSTRAINT ticket_statut_check CHECK (((statut)::text = ANY ((ARRAY['disponible'::character varying, 'vendu'::character varying])::text[])))
);


ALTER TABLE public.ticket OWNER TO jack;

--
-- Name: ticket_id_ticket_seq; Type: SEQUENCE; Schema: public; Owner: jack
--

CREATE SEQUENCE public.ticket_id_ticket_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ticket_id_ticket_seq OWNER TO jack;

--
-- Name: ticket_id_ticket_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jack
--

ALTER SEQUENCE public.ticket_id_ticket_seq OWNED BY public.ticket.id_ticket;


--
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: jack
--

CREATE TABLE public.utilisateur (
    id_utilisateur integer NOT NULL,
    nom character varying(100) NOT NULL,
    email character varying(150) NOT NULL,
    mot_de_passe text NOT NULL,
    role character varying(10) NOT NULL,
    CONSTRAINT utilisateur_role_check CHECK (((role)::text = ANY ((ARRAY['client'::character varying, 'admin'::character varying])::text[])))
);


ALTER TABLE public.utilisateur OWNER TO jack;

--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE; Schema: public; Owner: jack
--

CREATE SEQUENCE public.utilisateur_id_utilisateur_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utilisateur_id_utilisateur_seq OWNER TO jack;

--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jack
--

ALTER SEQUENCE public.utilisateur_id_utilisateur_seq OWNED BY public.utilisateur.id_utilisateur;


--
-- Name: match id_match; Type: DEFAULT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.match ALTER COLUMN id_match SET DEFAULT nextval('public.match_id_match_seq'::regclass);


--
-- Name: paiement id_paiement; Type: DEFAULT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.paiement ALTER COLUMN id_paiement SET DEFAULT nextval('public.paiement_id_paiement_seq'::regclass);


--
-- Name: ticket id_ticket; Type: DEFAULT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.ticket ALTER COLUMN id_ticket SET DEFAULT nextval('public.ticket_id_ticket_seq'::regclass);


--
-- Name: utilisateur id_utilisateur; Type: DEFAULT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.utilisateur ALTER COLUMN id_utilisateur SET DEFAULT nextval('public.utilisateur_id_utilisateur_seq'::regclass);


--
-- Name: match match_pkey; Type: CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.match
    ADD CONSTRAINT match_pkey PRIMARY KEY (id_match);


--
-- Name: paiement paiement_pkey; Type: CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_pkey PRIMARY KEY (id_paiement);


--
-- Name: ticket ticket_pkey; Type: CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (id_ticket);


--
-- Name: utilisateur utilisateur_email_key; Type: CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_email_key UNIQUE (email);


--
-- Name: utilisateur utilisateur_pkey; Type: CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (id_utilisateur);


--
-- Name: paiement paiement_id_ticket_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_id_ticket_fkey FOREIGN KEY (id_ticket) REFERENCES public.ticket(id_ticket) ON DELETE CASCADE;


--
-- Name: paiement paiement_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.paiement
    ADD CONSTRAINT paiement_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur) ON DELETE CASCADE;


--
-- Name: ticket ticket_id_match_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_id_match_fkey FOREIGN KEY (id_match) REFERENCES public.match(id_match) ON DELETE CASCADE;


--
-- Name: ticket ticket_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: jack
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

