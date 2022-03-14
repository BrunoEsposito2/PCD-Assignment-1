------------------------- MODULE step3_cs_ok_mutex -------------------------

EXTENDS TLC, Integers

(*--algorithm critical_section

variables mutex = 1;
  
define
    MutualExclusion == []~(pc["p1"] = "CS" /\ pc["p2"] = "CS")
end define;
    
macro wait(s) begin
  await s > 0;  
  s := s - 1;
end macro;

macro signal(s) begin
  s := s + 1;
end macro;


process proc \in {"p1", "p2"}
begin MainLoop:
  while TRUE do
    NCS: skip;
    wait(mutex);  
    CS: skip;
    signal(mutex);                    
  end while;
end process;


end algorithm;*)
\* BEGIN TRANSLATION (chksum(pcal) = "69d1829a" /\ chksum(tla) = "92c78dee")
VARIABLES mutex, pc

(* define statement *)
MutualExclusion == []~(pc["p1"] = "CS" /\ pc["p2"] = "CS")


vars == << mutex, pc >>

ProcSet == ({"p1", "p2"})

Init == (* Global variables *)
        /\ mutex = 1
        /\ pc = [self \in ProcSet |-> "MainLoop"]

MainLoop(self) == /\ pc[self] = "MainLoop"
                  /\ pc' = [pc EXCEPT ![self] = "NCS"]
                  /\ mutex' = mutex

NCS(self) == /\ pc[self] = "NCS"
             /\ TRUE
             /\ mutex > 0
             /\ mutex' = mutex - 1
             /\ pc' = [pc EXCEPT ![self] = "CS"]

CS(self) == /\ pc[self] = "CS"
            /\ TRUE
            /\ mutex' = mutex + 1
            /\ pc' = [pc EXCEPT ![self] = "MainLoop"]

proc(self) == MainLoop(self) \/ NCS(self) \/ CS(self)

Next == (\E self \in {"p1", "p2"}: proc(self))

Spec == Init /\ [][Next]_vars

\* END TRANSLATION 

=============================================================================
\* Modification History
\* Last modified Sun Mar 28 22:58:23 CEST 2021 by aricci
\* Created Sun Mar 28 15:14:35 CEST 2021 by aricci
