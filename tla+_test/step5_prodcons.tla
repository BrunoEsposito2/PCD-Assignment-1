--------------------------- MODULE step5_prodcons ---------------------------

EXTENDS TLC, Integers, Sequences
CONSTANTS MaxQueueSize

(*--algorithm message_queue
variables queue = <<>>, toProduce = 2, toProcess = 2, nReturned = 0,
        nWorkersHits = 0, nWorkers = 2, isAllowedToStart = FALSE, 
        isAllowedToContinue = FALSE, masterDone = FALSE,
        notAllInBarrier = FALSE, nConsHits = 0, allowedToContinue = FALSE;

define
  BoundedQueue == Len(queue) <= MaxQueueSize
end define;

process master = "master"
begin MainLoop:
  while TRUE do
    synchMasterWorkerMASTER:
        if nWorkersHits = nWorkers then 
            print "all processes hit the barrier signalAll";
            l7: 
                isAllowedToStart := TRUE;
                nWorkersHits := 0;
        end if;
    l4: 
        print "in l4";
        if isAllowedToContinue = TRUE then
            nReturned := 0;
            nConsHits := 0;
            notAllInBarrier := TRUE;
        end if;
  end while;
end process;

process producer \in { "prod1", "prod2" } 
variables x = 0;
begin Produce:
  while TRUE do
  synchMasterWorkerPROD:
        nWorkersHits := nWorkersHits + 1;
        print "a PROD process hit barrier";
        print nWorkersHits;
  if isAllowedToStart = TRUE then
  produce:
    x := 3 + 4;
    print x;
    toProduce := toProduce - 1; 
  produceContinue:  
    await toProduce <= 0;
    print "toProduce = 0";
    toProduce := 2;
  put:
    await Len(queue) < MaxQueueSize;
    queue := Append(queue, x);
  end if;
  end while;
end process;

process consumer \in { "cons1", "cons2" }
variables x = 0;
begin Consume:
  while TRUE do
    get:
        await queue /= <<>>;
        x := Head(queue);
        queue := Tail(queue);
        nConsHits := nConsHits + 1;
    consume:
        print x;
        toProcess := toProcess - 1;
    consumeContinue:
        await toProcess <= 0;
        print "toProcess = 0";
        toProcess := 2;
        isAllowedToContinue := TRUE;
    evaluateSynchronize:
        if nConsHits < 2 then
            await notAllInBarrier = TRUE;   
        else
            isAllowedToContinue := TRUE;                
        end if;
  end while;
end process;
end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "f9988a37" /\ chksum(tla) = "20af7f91")
\* Process variable x of process producer at line 37 col 11 changed to x_
VARIABLES queue, toProduce, toProcess, nReturned, nWorkersHits, nWorkers, 
          isAllowedToStart, isAllowedToContinue, masterDone, notAllInBarrier, 
          nConsHits, allowedToContinue, pc

(* define statement *)
BoundedQueue == Len(queue) <= MaxQueueSize

VARIABLES x_, x

vars == << queue, toProduce, toProcess, nReturned, nWorkersHits, nWorkers, 
           isAllowedToStart, isAllowedToContinue, masterDone, notAllInBarrier, 
           nConsHits, allowedToContinue, pc, x_, x >>

ProcSet == {"master"} \cup ({ "prod1", "prod2" }) \cup ({ "cons1", "cons2" })

Init == (* Global variables *)
        /\ queue = <<>>
        /\ toProduce = 2
        /\ toProcess = 2
        /\ nReturned = 0
        /\ nWorkersHits = 0
        /\ nWorkers = 2
        /\ isAllowedToStart = FALSE
        /\ isAllowedToContinue = FALSE
        /\ masterDone = FALSE
        /\ notAllInBarrier = FALSE
        /\ nConsHits = 0
        /\ allowedToContinue = FALSE
        (* Process producer *)
        /\ x_ = [self \in { "prod1", "prod2" } |-> 0]
        (* Process consumer *)
        /\ x = [self \in { "cons1", "cons2" } |-> 0]
        /\ pc = [self \in ProcSet |-> CASE self = "master" -> "MainLoop"
                                        [] self \in { "prod1", "prod2" } -> "Produce"
                                        [] self \in { "cons1", "cons2" } -> "Consume"]

MainLoop == /\ pc["master"] = "MainLoop"
            /\ pc' = [pc EXCEPT !["master"] = "synchMasterWorkerMASTER"]
            /\ UNCHANGED << queue, toProduce, toProcess, nReturned, 
                            nWorkersHits, nWorkers, isAllowedToStart, 
                            isAllowedToContinue, masterDone, notAllInBarrier, 
                            nConsHits, allowedToContinue, x_, x >>

synchMasterWorkerMASTER == /\ pc["master"] = "synchMasterWorkerMASTER"
                           /\ IF nWorkersHits = nWorkers
                                 THEN /\ PrintT("all processes hit the barrier signalAll")
                                      /\ pc' = [pc EXCEPT !["master"] = "l7"]
                                 ELSE /\ pc' = [pc EXCEPT !["master"] = "l4"]
                           /\ UNCHANGED << queue, toProduce, toProcess, 
                                           nReturned, nWorkersHits, nWorkers, 
                                           isAllowedToStart, 
                                           isAllowedToContinue, masterDone, 
                                           notAllInBarrier, nConsHits, 
                                           allowedToContinue, x_, x >>

l7 == /\ pc["master"] = "l7"
      /\ isAllowedToStart' = TRUE
      /\ nWorkersHits' = 0
      /\ pc' = [pc EXCEPT !["master"] = "l4"]
      /\ UNCHANGED << queue, toProduce, toProcess, nReturned, nWorkers, 
                      isAllowedToContinue, masterDone, notAllInBarrier, 
                      nConsHits, allowedToContinue, x_, x >>

l4 == /\ pc["master"] = "l4"
      /\ PrintT("in l4")
      /\ IF isAllowedToContinue = TRUE
            THEN /\ nReturned' = 0
                 /\ nConsHits' = 0
                 /\ notAllInBarrier' = TRUE
            ELSE /\ TRUE
                 /\ UNCHANGED << nReturned, notAllInBarrier, nConsHits >>
      /\ pc' = [pc EXCEPT !["master"] = "MainLoop"]
      /\ UNCHANGED << queue, toProduce, toProcess, nWorkersHits, nWorkers, 
                      isAllowedToStart, isAllowedToContinue, masterDone, 
                      allowedToContinue, x_, x >>

master == MainLoop \/ synchMasterWorkerMASTER \/ l7 \/ l4

Produce(self) == /\ pc[self] = "Produce"
                 /\ pc' = [pc EXCEPT ![self] = "synchMasterWorkerPROD"]
                 /\ UNCHANGED << queue, toProduce, toProcess, nReturned, 
                                 nWorkersHits, nWorkers, isAllowedToStart, 
                                 isAllowedToContinue, masterDone, 
                                 notAllInBarrier, nConsHits, allowedToContinue, 
                                 x_, x >>

synchMasterWorkerPROD(self) == /\ pc[self] = "synchMasterWorkerPROD"
                               /\ nWorkersHits' = nWorkersHits + 1
                               /\ PrintT("a PROD process hit barrier")
                               /\ PrintT(nWorkersHits')
                               /\ IF isAllowedToStart = TRUE
                                     THEN /\ pc' = [pc EXCEPT ![self] = "produce"]
                                     ELSE /\ pc' = [pc EXCEPT ![self] = "Produce"]
                               /\ UNCHANGED << queue, toProduce, toProcess, 
                                               nReturned, nWorkers, 
                                               isAllowedToStart, 
                                               isAllowedToContinue, masterDone, 
                                               notAllInBarrier, nConsHits, 
                                               allowedToContinue, x_, x >>

produce(self) == /\ pc[self] = "produce"
                 /\ x_' = [x_ EXCEPT ![self] = 3 + 4]
                 /\ PrintT(x_'[self])
                 /\ toProduce' = toProduce - 1
                 /\ pc' = [pc EXCEPT ![self] = "produceContinue"]
                 /\ UNCHANGED << queue, toProcess, nReturned, nWorkersHits, 
                                 nWorkers, isAllowedToStart, 
                                 isAllowedToContinue, masterDone, 
                                 notAllInBarrier, nConsHits, allowedToContinue, 
                                 x >>

produceContinue(self) == /\ pc[self] = "produceContinue"
                         /\ toProduce <= 0
                         /\ PrintT("toProduce = 0")
                         /\ toProduce' = 2
                         /\ pc' = [pc EXCEPT ![self] = "put"]
                         /\ UNCHANGED << queue, toProcess, nReturned, 
                                         nWorkersHits, nWorkers, 
                                         isAllowedToStart, isAllowedToContinue, 
                                         masterDone, notAllInBarrier, 
                                         nConsHits, allowedToContinue, x_, x >>

put(self) == /\ pc[self] = "put"
             /\ Len(queue) < MaxQueueSize
             /\ queue' = Append(queue, x_[self])
             /\ pc' = [pc EXCEPT ![self] = "Produce"]
             /\ UNCHANGED << toProduce, toProcess, nReturned, nWorkersHits, 
                             nWorkers, isAllowedToStart, isAllowedToContinue, 
                             masterDone, notAllInBarrier, nConsHits, 
                             allowedToContinue, x_, x >>

producer(self) == Produce(self) \/ synchMasterWorkerPROD(self)
                     \/ produce(self) \/ produceContinue(self) \/ put(self)

Consume(self) == /\ pc[self] = "Consume"
                 /\ pc' = [pc EXCEPT ![self] = "get"]
                 /\ UNCHANGED << queue, toProduce, toProcess, nReturned, 
                                 nWorkersHits, nWorkers, isAllowedToStart, 
                                 isAllowedToContinue, masterDone, 
                                 notAllInBarrier, nConsHits, allowedToContinue, 
                                 x_, x >>

get(self) == /\ pc[self] = "get"
             /\ queue /= <<>>
             /\ x' = [x EXCEPT ![self] = Head(queue)]
             /\ queue' = Tail(queue)
             /\ nConsHits' = nConsHits + 1
             /\ pc' = [pc EXCEPT ![self] = "consume"]
             /\ UNCHANGED << toProduce, toProcess, nReturned, nWorkersHits, 
                             nWorkers, isAllowedToStart, isAllowedToContinue, 
                             masterDone, notAllInBarrier, allowedToContinue, 
                             x_ >>

consume(self) == /\ pc[self] = "consume"
                 /\ PrintT(x[self])
                 /\ toProcess' = toProcess - 1
                 /\ pc' = [pc EXCEPT ![self] = "consumeContinue"]
                 /\ UNCHANGED << queue, toProduce, nReturned, nWorkersHits, 
                                 nWorkers, isAllowedToStart, 
                                 isAllowedToContinue, masterDone, 
                                 notAllInBarrier, nConsHits, allowedToContinue, 
                                 x_, x >>

consumeContinue(self) == /\ pc[self] = "consumeContinue"
                         /\ toProcess <= 0
                         /\ PrintT("toProcess = 0")
                         /\ toProcess' = 2
                         /\ isAllowedToContinue' = TRUE
                         /\ pc' = [pc EXCEPT ![self] = "evaluateSynchronize"]
                         /\ UNCHANGED << queue, toProduce, nReturned, 
                                         nWorkersHits, nWorkers, 
                                         isAllowedToStart, masterDone, 
                                         notAllInBarrier, nConsHits, 
                                         allowedToContinue, x_, x >>

evaluateSynchronize(self) == /\ pc[self] = "evaluateSynchronize"
                             /\ IF nConsHits < 2
                                   THEN /\ notAllInBarrier = TRUE
                                        /\ UNCHANGED isAllowedToContinue
                                   ELSE /\ isAllowedToContinue' = TRUE
                             /\ pc' = [pc EXCEPT ![self] = "Consume"]
                             /\ UNCHANGED << queue, toProduce, toProcess, 
                                             nReturned, nWorkersHits, nWorkers, 
                                             isAllowedToStart, masterDone, 
                                             notAllInBarrier, nConsHits, 
                                             allowedToContinue, x_, x >>

consumer(self) == Consume(self) \/ get(self) \/ consume(self)
                     \/ consumeContinue(self) \/ evaluateSynchronize(self)

Next == master
           \/ (\E self \in { "prod1", "prod2" }: producer(self))
           \/ (\E self \in { "cons1", "cons2" }: consumer(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Mon Apr 04 11:03:27 CEST 2022 by bruno
\* Last modified Sun Apr 03 16:31:30 CEST 2022 by Cecilia
\* Last modified Sat Apr 02 17:06:18 CEST 2022 by Camillo
\* Last modified Sun Mar 28 15:40:26 CEST 2021 by aricci
\* Created Sun Mar 28 08:34:06 CEST 2021 by aricci

=============================================================================
\* Modification History
\* Created Sun Mar 28 15:32:19 CEST 2021 by aricci
