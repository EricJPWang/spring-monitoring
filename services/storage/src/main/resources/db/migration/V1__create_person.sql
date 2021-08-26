create table IF NOT EXISTS person (
    id uuid default gen_random_uuid() not null constraint person_pk primary key,
    name text not null
);


