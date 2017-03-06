"use strict";

const eventEmiter = require ('./event-emiter');
const npc = require ('./npc');

/**
 * Ciclo principal
 */

//setInterval(() => {
	//eventEmiter.send("update");
	//eventEmiter.update();
//}, 500);


//  Posibles entradas "Hurt", "Element in", "Element out", "Heal" 
const NPC1 = new npc("NPC1");
NPC1.show();console.log("");
eventEmiter.send("Element in");
eventEmiter.update();NPC1.show();console.log("");
eventEmiter.send("Heal");
eventEmiter.update();NPC1.show();console.log("");
eventEmiter.send("Hurt");
eventEmiter.update();NPC1.show();console.log("");
eventEmiter.send("Hurt");
eventEmiter.update();NPC1.show();console.log("");
eventEmiter.send("Heal");
eventEmiter.update();NPC1.show();console.log("");