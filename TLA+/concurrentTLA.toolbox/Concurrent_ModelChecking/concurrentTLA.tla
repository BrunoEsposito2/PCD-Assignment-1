--------------------------- MODULE concurrentTLA ---------------------------

EXTENDS TLC, Integers, Sequences
CONSTANTS MaxQueueSize

(*--algorithm message_queue

variables queue = <<>>,
    \* Counter of the workers that have to arrive in barrier at the beginning 
    nWorkersHits = 0,
    \* Start prod/cons process condition 
    isAllowedToStart = FALSE,
    \* Start master process condition
    startMaster = TRUE,
    \* Initial number of workers to consider before start producing
    WORKERS_TO_WAIT = 2;

define
  BoundedQueue == Len(queue) <= MaxQueueSize
end define;

fair process master = "master"
begin Master:
    while TRUE do
        \* Waiting for control from consumers
        await startMaster;
        \* Blocking N workers in barrier before making them work
        if nWorkersHits = WORKERS_TO_WAIT then
            print "Workers are in barrier. Starting the prod/cons process!";
            \* Stop of the master to let the prod/cons proccess start
            startMaster := FALSE;
            isAllowedToStart := TRUE;
        \* Counting workers in barrier
        else
            nWorkersHits := nWorkersHits + 1;
        end if;
    end while;
end process;

fair process producer \in { "prod1", "prod2" } 
variable body = "";
begin Produce:
  while TRUE do
    \* Waiting the start command from the master 
    await isAllowedToStart;
    \* Producer starts producing
    produce:
        body := "body consumed";
    put: 
        \* Waiting to put the produced value in the buffer
        await Len(queue) < MaxQueueSize;
        \* Putting the produced value in the buffer
        queue := Append(queue, body);
        print "Body inserted in buffer";
  end while;
end process;

fair process consumer \in { "cons1", "cons2" }
variable body = "none";
begin Consume:
  while TRUE do
    take: 
        \* Waiting to take some produced value from the buffer
        await queue /= <<>> /\ isAllowedToStart;
        \* Consuming the produced value
        body := Head(queue);
        queue := Tail(queue);
    consume:
        print body;
    \* Update the number of workers that must produce 
    nWorkersHits := nWorkersHits - 1;
    \* Check if all workers have produced
    if nWorkersHits = 0 then
        \* Stop of the prod/cons proccess to give the control to the master
        print "Consumers have done. Giving control to the master.";
        isAllowedToStart := FALSE;
        startMaster := TRUE;
    end if;
  end while;
end process;
end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "ccff5c61" /\ chksum(tla) = "8b1de9c5")
\* Process variable body of process producer at line 41 col 10 changed to body_
VARIABLES queue, nWorkersHits, isAllowedToStart, startMaster, WORKERS_TO_WAIT, 
          pc

(* define statement *)
BoundedQueue == Len(queue) <= MaxQueueSize

VARIABLES body_, body

vars == << queue, nWorkersHits, isAllowedToStart, startMaster, 
           WORKERS_TO_WAIT, pc, body_, body >>

ProcSet == {"master"} \cup ({ "prod1", "prod2" }) \cup ({ "cons1", "cons2" })

Init == (* Global variables *)
        /\ queue = <<>>
        /\ nWorkersHits = 0
        /\ isAllowedToStart = FALSE
        /\ startMaster = TRUE
        /\ WORKERS_TO_WAIT = 2
        (* Process producer *)
        /\ body_ = [self \in { "prod1", "prod2" } |-> ""]
        (* Process consumer *)
        /\ body = [self \in { "cons1", "cons2" } |-> "none"]
        /\ pc = [self \in ProcSet |-> CASE self = "master" -> "Master"
                                        [] self \in { "prod1", "prod2" } -> "Produce"
                                        [] self \in { "cons1", "cons2" } -> "Consume"]

Master == /\ pc["master"] = "Master"
          /\ startMaster
          /\ IF nWorkersHits = WORKERS_TO_WAIT
                THEN /\ PrintT("Workers are in barrier. Starting the prod/cons process!")
                     /\ startMaster' = FALSE
                     /\ isAllowedToStart' = TRUE
                     /\ UNCHANGED nWorkersHits
                ELSE /\ nWorkersHits' = nWorkersHits + 1
                     /\ UNCHANGED << isAllowedToStart, startMaster >>
          /\ pc' = [pc EXCEPT !["master"] = "Master"]
          /\ UNCHANGED << queue, WORKERS_TO_WAIT, body_, body >>

master == Master

Produce(self) == /\ pc[self] = "Produce"
                 /\ isAllowedToStart
                 /\ pc' = [pc EXCEPT ![self] = "produce"]
                 /\ UNCHANGED << queue, nWorkersHits, isAllowedToStart, 
                                 startMaster, WORKERS_TO_WAIT, body_, body >>

produce(self) == /\ pc[self] = "produce"
                 /\ body_' = [body_ EXCEPT ![self] = "body consumed"]
                 /\ pc' = [pc EXCEPT ![self] = "put"]
                 /\ UNCHANGED << queue, nWorkersHits, isAllowedToStart, 
                                 startMaster, WORKERS_TO_WAIT, body >>

put(self) == /\ pc[self] = "put"
             /\ Len(queue) < MaxQueueSize
             /\ queue' = Append(queue, body_[self])
             /\ PrintT("Body inserted in buffer")
             /\ pc' = [pc EXCEPT ![self] = "Produce"]
             /\ UNCHANGED << nWorkersHits, isAllowedToStart, startMaster, 
                             WORKERS_TO_WAIT, body_, body >>

producer(self) == Produce(self) \/ produce(self) \/ put(self)

Consume(self) == /\ pc[self] = "Consume"
                 /\ pc' = [pc EXCEPT ![self] = "take"]
                 /\ UNCHANGED << queue, nWorkersHits, isAllowedToStart, 
                                 startMaster, WORKERS_TO_WAIT, body_, body >>

take(self) == /\ pc[self] = "take"
              /\ queue /= <<>> /\ isAllowedToStart
              /\ body' = [body EXCEPT ![self] = Head(queue)]
              /\ queue' = Tail(queue)
              /\ pc' = [pc EXCEPT ![self] = "consume"]
              /\ UNCHANGED << nWorkersHits, isAllowedToStart, startMaster, 
                              WORKERS_TO_WAIT, body_ >>

consume(self) == /\ pc[self] = "consume"
                 /\ PrintT(body[self])
                 /\ nWorkersHits' = nWorkersHits - 1
                 /\ IF nWorkersHits' = 0
                       THEN /\ PrintT("Consumers have done. Giving control to the master.")
                            /\ isAllowedToStart' = FALSE
                            /\ startMaster' = TRUE
                       ELSE /\ TRUE
                            /\ UNCHANGED << isAllowedToStart, startMaster >>
                 /\ pc' = [pc EXCEPT ![self] = "Consume"]
                 /\ UNCHANGED << queue, WORKERS_TO_WAIT, body_, body >>

consumer(self) == Consume(self) \/ take(self) \/ consume(self)

Next == master
           \/ (\E self \in { "prod1", "prod2" }: producer(self))
           \/ (\E self \in { "cons1", "cons2" }: consumer(self))

Spec == /\ Init /\ [][Next]_vars
        /\ WF_vars(master)
        /\ \A self \in { "prod1", "prod2" } : WF_vars(producer(self))
        /\ \A self \in { "cons1", "cons2" } : WF_vars(consumer(self))

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Wed Apr 06 17:32:49 CEST 2022 by bruno
\* Last modified Sun Mar 28 15:40:26 CEST 2021 by aricci
\* Created Sun Mar 28 08:34:06 CEST 2021 by aricci

=============================================================================
\* Modification History
\* Created Sun Mar 28 15:32:19 CEST 2021 by aricci
