--------------------------- MODULE step5_prodcons ---------------------------

EXTENDS TLC, Integers, Sequences
CONSTANTS MaxQueueSize

(*--algorithm message_queue

variables queue = <<>>, consumersDone = FALSE, producersControl = FALSE;

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
    consumersDone := TRUE;
  end while;
end process;
end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "3d891203" /\ chksum(tla) = "3707d34b")
\* Process variable item of process producer at line 28 col 10 changed to item_
VARIABLES queue, consumersDone, producersControl, pc

(* define statement *)
BoundedQueue == Len(queue) <= MaxQueueSize

VARIABLES item_, item

vars == << queue, consumersDone, producersControl, pc, item_, item >>

ProcSet == {"master"} \cup ({ "prod1", "prod2" }) \cup ({ "cons1", "cons2" })

Init == (* Global variables *)
        /\ queue = <<>>
        /\ consumersDone = FALSE
        /\ producersControl = FALSE
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
          /\ UNCHANGED << queue, item_, item >>

master == Master

Produce(self) == /\ pc[self] = "Produce"
                 /\ producersControl
                 /\ pc' = [pc EXCEPT ![self] = "produce"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, item_, 
                                 item >>

produce(self) == /\ pc[self] = "produce"
                 /\ item_' = [item_ EXCEPT ![self] = "item"]
                 /\ pc' = [pc EXCEPT ![self] = "put"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, item >>

put(self) == /\ pc[self] = "put"
             /\ Len(queue) < MaxQueueSize
             /\ queue' = Append(queue, item_[self])
             /\ producersControl' = FALSE
             /\ pc' = [pc EXCEPT ![self] = "Produce"]
             /\ UNCHANGED << consumersDone, item_, item >>

producer(self) == Produce(self) \/ produce(self) \/ put(self)

Consume(self) == /\ pc[self] = "Consume"
                 /\ pc' = [pc EXCEPT ![self] = "take"]
                 /\ UNCHANGED << queue, consumersDone, producersControl, item_, 
                                 item >>

take(self) == /\ pc[self] = "take"
              /\ queue /= <<>>
              /\ item' = [item EXCEPT ![self] = Head(queue)]
              /\ queue' = Tail(queue)
              /\ pc' = [pc EXCEPT ![self] = "consume"]
              /\ UNCHANGED << consumersDone, producersControl, item_ >>

consume(self) == /\ pc[self] = "consume"
                 /\ PrintT(item[self])
                 /\ consumersDone' = TRUE
                 /\ pc' = [pc EXCEPT ![self] = "Consume"]
                 /\ UNCHANGED << queue, producersControl, item_, item >>

consumer(self) == Consume(self) \/ take(self) \/ consume(self)

Next == master
           \/ (\E self \in { "prod1", "prod2" }: producer(self))
           \/ (\E self \in { "cons1", "cons2" }: consumer(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Mon Apr 04 15:48:22 CEST 2022 by bruno
\* Last modified Sun Mar 28 15:40:26 CEST 2021 by aricci
\* Created Sun Mar 28 08:34:06 CEST 2021 by aricci

=============================================================================
\* Modification History
\* Created Sun Mar 28 15:32:19 CEST 2021 by aricci
