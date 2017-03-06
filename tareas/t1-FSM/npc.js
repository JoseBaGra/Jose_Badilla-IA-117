"use strict";

const State = require('./state');
const fsm = require('./fsm');
const eventEmitter = require('./event-emiter');

/*
 *Resting
 *
 *Hurt -> Annoyed
 *Element in -> Angry
*/
class Resting extends State{
	accepts(event,current) {
		var response = false;
		if(event.msg === "Heal" && current instanceof Annoyed){response = true;}
		else if(event.msg === "Element out" && current instanceof Annoyed){response = true;}

		if(response){console.log(current.constructor.name + " X " + event.msg + " -> " + this.constructor.name);}
		return response;
	}

	onEnter(eventEmitter, fsm) {
		fsm.owner().rest();
	}

	onUpdate(eventEmitter, fsm) {
		fsm.owner().show();
	}
}

/*
 *Angry
 *
 *Hurt -> Furious
 *Heal -> Annoyed
*/
class Angry extends State{
	accepts(event,current) {
		var response = false;
		if(event.msg === "Hurt" && current instanceof Resting){response = true;}
		else if(event.msg === "Hurt" && current instanceof Annoyed){response = true;}
		else if(event.msg === "Heal" && current instanceof Furious){response = true;}
		
		if(response){console.log(current.constructor.name + " X " + event.msg + " -> " + this.constructor.name);}
		return response;

	}

	onEnter(eventEmitter, fsm) {
		fsm.owner().angry();
	}

	onUpdate(eventEmitter, fsm) {
		fsm.owner().show();
	}
}

/*
 *Furious
 *
 *Hurt -> Furious
 *Heal -> Angry
*/
class Furious extends State{
	accepts(event,current) {
		var response = false;
		if(event.msg === "Hurt" && current instanceof Angry){response = true;}
		if(response){console.log(current.constructor.name + " X " + event.msg + " -> " + this.constructor.name);}
		return response;
	}

	onEnter(eventEmitter, fsm) {
		fsm.owner().furious();
	}

	onUpdate(eventEmitter, fsm) {
		fsm.owner().show();
	}
}

/*
 *Annoyed
 *
 *Hurt -> Angry
 *Heal -> Resting
 *Element out -> Resting
*/
class Annoyed extends State{
	accepts(event,current) {
		var response = false;
		if(event.msg === "Element in" && current instanceof Resting){response = true;}
		if(response){console.log(current.constructor.name + " X " + event.msg + " -> " + this.constructor.name);}
		return response;
	}

	onEnter(eventEmitter, fsm) {
		fsm.owner().annoyed();
	}

	onUpdate(eventEmitter, fsm) {
		fsm.owner().show();
	}
}

const states = [new Resting(), new Angry(), new Furious(), new Annoyed()];

class NPC {
 	constructor(id) {
		this._id = id;
		this._state = states[0].constructor.name;
		const myFSM = new fsm(this, states);
		eventEmitter.register(myFSM);
  	}

	id() {
		return this._id;
	}

	rest(){
		this._state = "Resting";	
	}
	angry(){
		this._state = "Angry";
	}
	furious(){
		this._state = "Furious";
	}
	annoyed(){
		this._state = "Annoyed";
	}

	show() {
		console.log("[" + this.constructor.name + "] Name: " + this.id() + ", State: " + this._state);
	}
}

module.exports = NPC