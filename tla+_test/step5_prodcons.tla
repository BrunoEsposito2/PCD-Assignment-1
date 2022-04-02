--------------------------- MODULE step5_prodcons ---------------------------

EXTENDS TLC, Integers, Sequences
CONSTANTS MaxQueueSize

(*--algorithm message_queue
variables queue = <<>>, toProduce = 0, toProcess = 0, 
          prodHits = 0, nProd = 2, signalProdBarrier = FALSE, signalContinue = FALSE, 
          barrier = FALSE;

define
  BoundedQueue == Len(queue) <= MaxQueueSize 
end define;

macro synchMasterWorker() begin
    toProduce := 2;
    prodHits := prodHits + 1;
    if prodHits = nProd then 
        print "a process hit barrier"
        signalProdBarrier := TRUE;
        prodHits := 0;
    else
        print "all processes hit the barrier: signalAll"
        await signalProdBarrier = TRUE;
        signalProdBarrier := FALSE;
    end if;
end macro;
    
process master \in { "master" }
begin MainLoop:
  while TRUE do
      signalContinue := FALSE;
    startAndWaitWorkers:
        toProcess := 4;
        synchMasterWorker:
            synchMasterWorker();
        await signalContinue = TRUE;

  end while;
end process;

process producer \in { "prod1", "prod2" } 
variables x = 0;
begin Produce:
  while TRUE do
    produce:
        await toProduce > 0;
        if toProduce > 0 then
            x := 3 + 4;
            toProduce := toProduce - 1; 
        else
            toProduce := 0;
            print "toProduce = 0";        
        end if;
    put: 
        await Len(queue) < MaxQueueSize;
        queue := Append(queue, x);
  end while;
end process;

process consumer \in { "cons1", "cons2" }
variables x = 0;
begin Consume:
  while TRUE do
    take: 
        await queue /= <<>>;
        x := Head(queue);
        queue := Tail(queue);
    consume:
        await toProcess > 0;
        if toProcess > 0 then
            print x;
            toProcess := toProcess - 1;
        else
            signalContinue := TRUE;
            print "toProcess = 0";
        \*    barrier := TRUE;
        end if;
  end while;
end process;
end algorithm;*)

\* BEGIN TRANSLATION (chksum(pcal) = "b6881a9a" /\ chksum(tla) = "3be8a852")
\* Process variable x of process producer at line 26 col 11 changed to x_
VARIABLES queue, toProduce, toProcess, barrier, pc

(* define statement *)
BoundedQueue == Len(queue) <= MaxQueueSize

VARIABLES x_, x

vars == << queue, toProduce, toProcess, barrier, pc, x_, x >>

ProcSet == ({ "master" }) \cup ({ "prod1", "prod2" }) \cup ({ "cons1", "cons2" })

Init == (* Global variables *)
        /\ queue = <<>>
        /\ toProduce = 0
        /\ toProcess = 0
        /\ barrier = FALSE
        (* Process producer *)
        /\ x_ = [self \in { "prod1", "prod2" } |-> 0]
        (* Process consumer *)
        /\ x = [self \in { "cons1", "cons2" } |-> 0]
        /\ pc = [self \in ProcSet |-> CASE self \in { "master" } -> "MainLoop"
                                        [] self \in { "prod1", "prod2" } -> "Produce"
                                        [] self \in { "cons1", "cons2" } -> "Consume"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ pc' = [pc EXCEPT ![self] = "p"]
                  /\ UNCHANGED << queue, toProduce, toProcess, barrier, x_, x >>

p(self) == /\ pc[self] = "p"
           /\ IF toProcess = 0 /\ toProduce = 0
                 THEN /\ toProcess' = 5
                      /\ toProduce' = 5
                 ELSE /\ TRUE
                      /\ UNCHANGED << toProduce, toProcess >>
           /\ pc' = [pc EXCEPT ![self] = "MainLoop"]
           /\ UNCHANGED << queue, barrier, x_, x >>

master(self) == MainLoop(self) \/ p(self)

Produce(self) == /\ pc[self] = "Produce"
                 /\ pc' = [pc EXCEPT ![self] = "produce"]
                 /\ UNCHANGED << queue, toProduce, toProcess, barrier, x_, x >>

produce(self) == /\ pc[self] = "produce"
                 /\ toProduce > 0
                 /\ IF toProduce > 0
                       THEN /\ x_' = [x_ EXCEPT ![self] = 3 + 4]
                            /\ toProduce' = toProduce - 1
                       ELSE /\ toProduce' = 0
                            /\ PrintT("toProduce = 0")
                            /\ x_' = x_
                 /\ pc' = [pc EXCEPT ![self] = "put"]
                 /\ UNCHANGED << queue, toProcess, barrier, x >>

put(self) == /\ pc[self] = "put"
             /\ Len(queue) < MaxQueueSize
             /\ queue' = Append(queue, x_[self])
             /\ pc' = [pc EXCEPT ![self] = "Produce"]
             /\ UNCHANGED << toProduce, toProcess, barrier, x_, x >>

producer(self) == Produce(self) \/ produce(self) \/ put(self)

Consume(self) == /\ pc[self] = "Consume"
                 /\ pc' = [pc EXCEPT ![self] = "take"]
                 /\ UNCHANGED << queue, toProduce, toProcess, barrier, x_, x >>

take(self) == /\ pc[self] = "take"
              /\ queue /= <<>>
              /\ x' = [x EXCEPT ![self] = Head(queue)]
              /\ queue' = Tail(queue)
              /\ pc' = [pc EXCEPT ![self] = "consume"]
              /\ UNCHANGED << toProduce, toProcess, barrier, x_ >>

consume(self) == /\ pc[self] = "consume"
                 /\ toProcess > 0
                 /\ IF toProcess > 0
                       THEN /\ PrintT(x[self])
                            /\ toProcess' = toProcess - 1
                       ELSE /\ toProcess' = 0
                            /\ PrintT("toProcess = 0")
                 /\ pc' = [pc EXCEPT ![self] = "Consume"]
                 /\ UNCHANGED << queue, toProduce, barrier, x_, x >>

consumer(self) == Consume(self) \/ take(self) \/ consume(self)

Next == (\E self \in { "master" }: master(self))
           \/ (\E self \in { "prod1", "prod2" }: producer(self))
           \/ (\E self \in { "cons1", "cons2" }: consumer(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Sat Apr 02 17:06:18 CEST 2022 by Camillo
\* Last modified Sat Apr 02 12:38:54 CEST 2022 by Cecilia
\* Last modified Sun Mar 28 15:40:26 CEST 2021 by aricci
\* Created Sun Mar 28 08:34:06 CEST 2021 by aricci

=============================================================================
\* Modification History
\* Created Sun Mar 28 15:32:19 CEST 2021 by aricci
