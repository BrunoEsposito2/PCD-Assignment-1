--------------------------- MODULE step5_prodcons ---------------------------

EXTENDS TLC, Integers, Sequences
CONSTANTS MaxQueueSize

(*--algorithm message_queue

variables queue = <<>>, consumersDone = FALSE, producersControl = FALSE,
        nConsumed = 0, nProduced = 0;

define
  BoundedQueue == Len(queue) <= MaxQueueSize 
end define;

process master = "master"
begin Master:
    while TRUE do
        if consumersDone = FALSE then
            producersControl := TRUE;
        else 
            await consumersDone;
            print "Restart master/workers process";
            consumersDone := FALSE;
        end if;
    end while;
end process;

process producer \in { "prod1", "prod2" } 
variable item = "";
begin Produce:
  while TRUE do
    await producersControl;
    produce:
        item := "item";
        incProduced: nProduced := nProduced + 1;
    put: 
        await Len(queue) < MaxQueueSize;
        queue := Append(queue, item);
    producersControl := FALSE;
  end while;
end process;

process consumer \in { "cons1", "cons2" }
variable item = "none";
begin Consume:
  while TRUE do
    take: 
        await queue /= <<>>;
        item := Head(queue);
        queue := Tail(queue);
    consume:
        print item;
        incConsumed: nConsumed := nConsumed + 1;
    if nConsumed = nProduced then
        resetConsumed:  nConsumed := 0;
        resetProduced:  nProduced := 0;
        consumersDone := TRUE;
    end if;
  end while;
end process;
end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "be19ec4a" /\ chksum(tla) = "e6be3a9b")
\* Process variable item of process producer at line 29 col 10 changed to item_
VARIABLES queue, consumersDone, producersControl, nConsumed, nProduced, pc

(* define statement *)
BoundedQueue == Len(queue) <= MaxQueueSize

VARIABLES item_, item

vars == << queue, consumersDone, producersControl, nConsumed, nProduced, pc, 
           item_, item >>

ProcSet == {"master"} \cup ({ "prod1", "prod2" }) \cup ({ "cons1", "cons2" })

Init == (* Global variables *)
        /\ queue = <<>>
        /\ consumersDone = FALSE
        /\ producersControl = FALSE
        /\ nConsumed = 0
        /\ nProduced = 0
        (* Process producer *)
        /\ item_ = [self \in { "prod1", "prod2" } |-> ""]
        (* Process consumer *)
        /\ item = [self \in { "cons1", "cons2" } |-> "none"]
        /\ pc = [self \in ProcSet |-> CASE self = "master" -> "Master"
                                        [] self \in { "prod1", "prod2" } -> "Produce"
                                        [] self \in { "cons1", "cons2" } -> "Consume"]

Master == /\ pc["master"] = "Master"
          /\ IF consumersDone = FALSE
                THEN /\ producersControl' = TRUE
                     /\ UNCHANGED consumersDone
                ELSE /\ consumersDone
                     /\ PrintT("Restart master/workers process")
                     /\ consumersDone' = FALSE
                     /\ UNCHANGED producersControl
          /\ pc' = [pc EXCEPT !["master"] = "Master"]
          /\ UNCHANGED << queue, nConsumed, nProduced, item_, item >>

master == Master

Produce(self) == /\ pc[self] = "Produce"
                 /\ producersControl
                 /\ pc' = [pc EXCEPT ![self] = "produce"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, 
                                 nConsumed, nProduced, item_, item >>

produce(self) == /\ pc[self] = "produce"
                 /\ item_' = [item_ EXCEPT ![self] = "item"]
                 /\ pc' = [pc EXCEPT ![self] = "incProduced"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, 
                                 nConsumed, nProduced, item >>

incProduced(self) == /\ pc[self] = "incProduced"
                     /\ nProduced' = nProduced + 1
                     /\ pc' = [pc EXCEPT ![self] = "put"]
                     /\ UNCHANGED << queue, consumersDone, producersControl, 
                                     nConsumed, item_, item >>

put(self) == /\ pc[self] = "put"
             /\ Len(queue) < MaxQueueSize
             /\ queue' = Append(queue, item_[self])
             /\ producersControl' = FALSE
             /\ pc' = [pc EXCEPT ![self] = "Produce"]
             /\ UNCHANGED << consumersDone, nConsumed, nProduced, item_, item >>

producer(self) == Produce(self) \/ produce(self) \/ incProduced(self)
                     \/ put(self)

Consume(self) == /\ pc[self] = "Consume"
                 /\ pc' = [pc EXCEPT ![self] = "take"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, 
                                 nConsumed, nProduced, item_, item >>

take(self) == /\ pc[self] = "take"
              /\ queue /= <<>>
              /\ item' = [item EXCEPT ![self] = Head(queue)]
              /\ queue' = Tail(queue)
              /\ pc' = [pc EXCEPT ![self] = "consume"]
              /\ UNCHANGED << consumersDone, producersControl, nConsumed, 
                              nProduced, item_ >>

consume(self) == /\ pc[self] = "consume"
                 /\ PrintT(item[self])
                 /\ pc' = [pc EXCEPT ![self] = "incConsumed"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, 
                                 nConsumed, nProduced, item_, item >>

incConsumed(self) == /\ pc[self] = "incConsumed"
                     /\ nConsumed' = nConsumed + 1
                     /\ IF nConsumed' = nProduced
                           THEN /\ pc' = [pc EXCEPT ![self] = "resetConsumed"]
                           ELSE /\ pc' = [pc EXCEPT ![self] = "Consume"]
                     /\ UNCHANGED << queue, consumersDone, producersControl, 
                                     nProduced, item_, item >>

resetConsumed(self) == /\ pc[self] = "resetConsumed"
                       /\ nConsumed' = 0
                       /\ pc' = [pc EXCEPT ![self] = "resetProduced"]
                       /\ UNCHANGED << queue, consumersDone, producersControl, 
                                       nProduced, item_, item >>

resetProduced(self) == /\ pc[self] = "resetProduced"
                       /\ nProduced' = 0
                       /\ consumersDone' = TRUE
                       /\ pc' = [pc EXCEPT ![self] = "Consume"]
                       /\ UNCHANGED << queue, producersControl, nConsumed, 
                                       item_, item >>

consumer(self) == Consume(self) \/ take(self) \/ consume(self)
                     \/ incConsumed(self) \/ resetConsumed(self)
                     \/ resetProduced(self)

Next == master
           \/ (\E self \in { "prod1", "prod2" }: producer(self))
           \/ (\E self \in { "cons1", "cons2" }: consumer(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Mon Apr 04 16:21:36 CEST 2022 by bruno
\* Last modified Sun Mar 28 15:40:26 CEST 2021 by aricci
\* Created Sun Mar 28 08:34:06 CEST 2021 by aricci

=============================================================================
\* Modification History
\* Created Sun Mar 28 15:32:19 CEST 2021 by aricci
