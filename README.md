# IoT Final Project 2023
This project has implemented the oneM2M attack, mainly focusing on the Flooding attacks, which can be categorized into: 
- AE Flooding (AF): create/retrieve an AE resource multiple times.
- Containers Flooding (CsF): create/ retrieve all the container resources of a given AE multiple times 
-  Container Flooding (CF): create/ retrieve one container resource of a given AE multiple times
- ContentInstance Flooding (CIF): create/ retrieve one contentInstance resource of a given container of a given AE multiple times
- Subscription Flooding (SF): create/ retrieve one subscription resource of a given container of a given AE multiple times
- Various Flooding (VF): create/ retrieve various resources from a given CSEBase multiple times
  
## Utilized Tools
- Eclipse (IPE implementation)
- Node-Red (MN-AE)
- Jupyter Notebook (Attacker)
- MIT App Inventor (Attack Monitor)

## OneM2M Structure
![image](https://github.com/GuFangYi/IoT_Final/assets/44097552/ab95011b-4b87-4f8b-8683-56e469f6f796)

## Simulation Flow
1. Start up IN-CSE and MN-CSE
2. Create monitor-ae in MN-CSE, within which a container (monitor-cnt) exists and it stores the attack types of all the attacks executed by the attacker as its contentInstance
3. Start the attack displayer (monitor), which monitors the monitor-cnt. There are two main inputs for changing the thresholds that determine whether the actions are attacks: (1) *Attack threshold*: the number of actions executed within a given time, (2)  *Delay threshold*: the time (in second) that fractions the attacks. To determine the attack type, there is a delay threshold, if the request stops for (delay threshold) seconds, then we fraction all the previous requests as one attack. And to determine whether the actions are considered attack, there is an attack threshold, if the number of actions exceed (attack threshold) times, then we consider these actions as an attack. For example, delay threshold is 3 seconds and attack threshold is 5 times. Now if an attacker performs an AE attack, and retrieves the AE resource 10 times in a row, which is done in 5 seconds, and the attack stops, then on the 8th seconds, the attack monitor displays the type of attack these requests are, and since 10 times > attack threshold, it displays **AF**.
4. Perform attack. With Jupyter notebook, we send the requests to create or retrieve the resources. Since the attacks can be in various form (oneM2M or non-oneM2M), I simulate it in a simple way such as sending **post_ae**, **get_cnt**, etc., and the request passes through the IPE and executes the request. The **post_XX** creates resources XX in IN-CSE, and **get_XX** retrieves resources XX and can be observed from the terminal.
5. While the attacks are being executed, we can observe the attack type real time with the attack monitor developed with MIT app inventor.

## Example Resource Trees
![incse](https://github.com/GuFangYi/IoT_Final/assets/44097552/6b72d052-a52f-46af-a941-b8e920680ffe)
![mncse](https://github.com/GuFangYi/IoT_Final/assets/44097552/1d5f449a-f6af-4f18-970f-fbd501539a4c)


