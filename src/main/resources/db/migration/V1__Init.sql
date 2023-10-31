SET DB_CLOSE_DELAY -1;
CREATE MEMORY TABLE "PUBLIC"."user_data"(

    "user_id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,

    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    "first_name" CHARACTER VARYING(20) NOT NULL,

    "last_name" CHARACTER VARYING(20) NOT NULL,

    "national_id_number" CHARACTER VARYING(15)

);
CREATE MEMORY TABLE "PUBLIC"."transaction"(

    "id" CHARACTER VARYING(255) NOT NULL,

    "amount" NUMERIC(19, 2),

    "bank_account_id" BIGINT,

    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    "currency" CHARACTER VARYING(3) NOT NULL,

    "fee" NUMERIC(19, 2),

    "operation" CHARACTER VARYING(255),

    "provider_id" CHARACTER VARYING(255),

    "status" CHARACTER VARYING(255),

    "user_id" BIGINT,

    "wallet_transaction_id" BIGINT

);
CREATE MEMORY TABLE "PUBLIC"."bank_account"(

    "bank_account_id" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL,

    "account_number" CHARACTER VARYING(10) NOT NULL,

    "bank_name" CHARACTER VARYING(20) NOT NULL,

    "created_at" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    "currency" CHARACTER VARYING(3) NOT NULL,

    "routing_number" CHARACTER VARYING(10) NOT NULL,

    "user_id" BIGINT

);
ALTER TABLE "PUBLIC"."user_data" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1" PRIMARY KEY("user_id");
ALTER TABLE "PUBLIC"."transaction" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_7" PRIMARY KEY("id");
ALTER TABLE "PUBLIC"."bank_account" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_9" PRIMARY KEY("bank_account_id");
ALTER TABLE "PUBLIC"."transaction"
    ADD FOREIGN KEY ("user_id")
        REFERENCES "PUBLIC"."user_data"("user_id");
ALTER TABLE "PUBLIC"."transaction"
    ADD FOREIGN KEY ("bank_account_id")
        REFERENCES "PUBLIC"."bank_account"("bank_account_id");
ALTER TABLE "PUBLIC"."bank_account"
    ADD FOREIGN KEY ("user_id")
        REFERENCES "PUBLIC"."user_data"("user_id");