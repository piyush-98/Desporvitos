


var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var cors = require('cors');
const Cos = require('@coschain/cosjs');

let cos = new Cos("test", "https://testnode.contentos.io");

cos.wallet.addAccount("gameoasisacc1", "3HYBMpSyDxwTgccTZLvyjg3RwJvjXwT6kH4kQd2TTFRtHqnNLj");
cos.wallet.addAccount("gameoasisacc2", "4onhFpwJaFUTXpJnKHpdSXy1e1XANBMk6yW9aQhqJ99eN4tKj1");
cos.wallet.addAccount("gameoasissport", "3wogY53eHhf56wtiTfQDmHqbWeuLDiSaTfkrkXnbNRjRjAMzWy");
cos.wallet.addAccount("gameoasisacc3", "42vNJEejccYm83QkM6UEwUxMRDNn8E6mAkXZptV5TU8rjGS698");
cos.wallet.addAccount("gameoasisacc4", "3awcKNDacK46GsqkiWkGJrJfRNtTY3TQNJ5k89jBUPpA7hqtkT");
cos.wallet.addAccount("gameoasisacc5", "3KpsEbMsW1uXorQnTUCj8axMQLKwkerhoNPtQrGLdPSyAPxUKi");


app.use(cors());
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded

// for transfer of winnings
app.get('/transfercos/:acct/:amt',function(req,res){
	
    (async() => {
        let result = await cos.wallet.transfer("gameoasissport", req.params.acct, req.params.amt+".000000", "memo");
        console.log(result);
        res.send(result.invoice.trxId);

    })();
    
});



// create INTEGER type question 
app.get('/createINTQuestion/:qid/:quest/:rate/:participants/:acct',function(req,res){
	(async() => {
        let result = await cos.wallet.contractCall(req.params.acct, "gameoasissport", "oursport", "createquest", 
        "["+req.params.qid+",\""+req.params.acct+"\",1,\""+
        req.params.quest+"\",1,"+req.params.rate+","
        +req.params.participants+",[]]"
        , "0.000000");
        console.log(result.invoice);
        res.send(result.invoice.trxId);

	  })();

      

});


// create MCQ question 
app.get('/createMCQQuestion/:qid/:quest/:rate/:participants/:acct',function(req,res){
    
   // var array = req.params.options.split(",");

	(async() => {
        let result = await cos.wallet.contractCall(req.params.acct, "gameoasissport", "oursport", "createquest", 
        "["+req.params.qid+",\""+req.params.acct+"\",1,\""+
        req.params.quest+"\",1,"+req.params.rate+","
        +req.params.participants+",[1,2]]"
       , "0.000000");
        console.log(result.invoice);
        res.send(result.invoice.trxId);

	  })();

});


// whitelist
app.get('/whitelist/:acct',function(req,res){
  
	(async() => {
        let result = await cos.wallet.contractCall("gameoasissport", "gameoasissport", "oursport", "verify_acct", 
        "[3,\""+req.params.acct+"\"]"
        , "0.000000");
        console.log(result.invoice);
        res.send(result.invoice.trxId);
	  })();


});


// vote the answer
// amt should be participation fee
app.get('/vote/:qid/:ans/:amt/:acct',function(req,res){

	(async() => {
        let result = await cos.wallet.contractCall(req.params.acct, "gameoasissport", "oursport", "vote", 
        "[\""+req.params.acct+"\","+req.params.qid+","+req.params.ans+"]"
        , req.params.amt+".00000");
        console.log(result.invoice);
        res.send(result.invoice.trxId);
	  })();


});

// submit the answer
app.get('/submitSolution/:qid/:ans',function(req,res){

	(async() => {
        let result = await cos.wallet.contractCall("gameoasissport", "gameoasissport", "oursport", "submitSolution", 
        "["+req.params.qid+","+req.params.ans+"]"
        , "0.000000");
        console.log(result.invoice);
        res.send(result.invoice.trxId);
	  })();


});


// check the balance
app.get('/appbalance',function(req,res){

    
    (async () => {
        let result = await cos.wallet.queryTable("gameoasissport", "oursport", "releases", "owner", '', 30, false);
        let r = result["tableContent"];   
        console.log(r);   
        res.send(r);
      })();
    

});

// check the COS balance
app.get('/balance/:accname',function(req,res){

    (async () => {
        let result = await cos.wallet.accountInfo(req.params.accname);
        console.log(result["info"]["coin"]);
        let amt =(result["info"]["coin"]["value"]/1000000).toFixed(6);
        console.log(amt);    

        res.send(amt);
        
      })();
  
});



// SPO sports token, can be integrated if required different token system.
// currently using COS for all txns
app.get('/transferSPO',function(req,res){
  
	//create	
	// (async() => {
	// 	let result = await cos.wallet.contractCall("gameoasisacc1", "gameoasisacc1", "gametesttoken", "create", "[\"sports\",\"SPO\",1000000000,4]", "0.000000");
	// 	console.log(result.invoice);
	//   })();

	(async() => {
		let result = await cos.wallet.contractCall("gameoasisacc1", "gameoasisacc1", "gametesttoken", "transfer", "[\"gameoasisacc1\",\"gameoasisacc2\",100]", "0.000000");
        console.log(result.invoice);
        res.send(result.invoice.trxId);

	  })();

});


app.listen(process.env.PORT || 8080, () => console.log('my app listening on port 8080!'))
