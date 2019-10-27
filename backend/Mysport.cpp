#include <cosiolib/contract.hpp>
#include <cosiolib/print.hpp>
#include <cosiolib/system.hpp>

using namespace std;

//https://testexplorer.contentos.io/#/contract-detail/gametesttoken/gameoasisacc1


struct question
{
    /* data */
    uint64_t q_id;
    cosio::name creator;
    uint64_t match_id;
    string quest;
    uint64_t q_type; // 1-> Integer type, 2-> MCQ
    uint64_t p_fee; // participation fee
    uint64_t max_participants;
    uint64_t status; // 1-> live, 0-> upcoming, 2-> declared
    vector<uint64_t> options; // null in case of Integer types
    uint64_t solution; // after declaration of ans by live feed, MCQ-> option no.
    
    COSIO_SERIALIZE(question, (q_id)(creator)(match_id)(quest)(q_type)(p_fee)(max_participants)(status)(options)(solution))
};

struct voter
{
    uint64_t q_id;
    vector<cosio::name> vtr;
    //uint64_t power;

    COSIO_SERIALIZE(voter, (q_id)(vtr))

};

struct hold
{
    uint64_t q_id;
    uint64_t hold_amount;

    COSIO_SERIALIZE(hold, (q_id)(hold_amount))

};

struct submission{

    cosio::name vtr; // voter acc name

    //std::map<std::string, std::string> submitted_ans; // qid -> ans //didn't work
    // below is hack for using maps...will be removed later
    vector<uint64_t> qid;
    vector<uint64_t> ans;
    
    COSIO_SERIALIZE(submission, (vtr)(qid)(ans))
};

struct matchstat
{
    uint64_t match_id;
    string team1;
    string team2;
    string venue;
    string date;// later replace by some time datatype

    COSIO_SERIALIZE(matchstat, (match_id)(team1)(team2)(venue)(date))

};


struct release {
    cosio::name owner;         ///< name of account who owns the token
    uint64_t amount;                ///< balance of the account

    COSIO_SERIALIZE(release, (owner)(amount))
};


// scope _self
struct whitelist {
    int8_t verify_level;
    cosio::name account;

    COSIO_SERIALIZE(whitelist, (verify_level)(account))
};



/**
 * @brief the Sports contract class
 */
struct mySport : public cosio::contract {
    using cosio::contract::contract;


  
    void createquest(uint64_t qid, cosio::name creator, uint64_t match_id,
                string quest, 
                uint64_t q_type, uint64_t p_fee,
                uint64_t max_participants
                , vector<uint64_t> options
                ){
     
        cosio::require_auth(creator);
    
        questions.insert([&](question& q){
            q.q_id = qid;
            q.creator = creator;
            q.match_id = match_id; 
            q.quest = quest;
            q.q_type = q_type;
            q.p_fee = p_fee;
            q.max_participants = max_participants;
            q.status = 1;
            q.options.assign(options.begin(),options.end());
            q.solution = 0;
        });

      cosio::print_f("Question created with question id%\n",qid);


    }



    /**
     * @brief contract method to transfer tokens.
     * 
     * @param from      the account who sends tokens.
     * @param to        the account who receives tokens.
     * @param amount    number of tokens to transfer.
     */

    // vote will be inter contract comm. from gametesttoken
    void vote(cosio::name from, uint64_t qid, uint64_t ans, uint64_t amount) {
        // we need authority of sender account.
        cosio::require_auth(from);

        // // check if sender has any tokens.
        // cosio::cosio_assert(balances.has(from), std::string("no balance:") + from.string());
        // // check if sender has enough tokens.
        // cosio::cosio_assert(balances.get(from).amount >= amount, std::string("balance not enough:") + from.string());
        // // check integer overflow
        // cosio::cosio_assert(balances.get_or_default(to).amount + amount > balances.get_or_default(to).amount, std::string("over flow"));
      
        auto b = voters.get_or_default(qid);
        
        vector<cosio::name> new_voters = b.vtr;
        new_voters.push_back(from);

        voters.insert([&](voter& v){
            v.q_id = qid;
            v.vtr.assign(new_voters.begin(),new_voters.end());
        });

        // add fee to holds table for particular qid

        if(!holds.has(qid)) {

          holds.insert([&](hold& h){
            h.q_id = qid;
            h.hold_amount = amount;
          });
        }  
        // modify
        else {

          holds.update(qid, [&](hold& h){
            h.hold_amount += amount;
          });

        }

        // // using maps
        // if(!allsubmissions.has(from)) {
        //   map<int,int> m;
        // //  m[qid] = ans;

        //     allsubmissions.insert([&](submissions& b){
        //                 b.vtr = from;
        //                 b.submitted_ans = m;
        //             });
        // } else {
            
        //     // auto a = allsubmissions.get(from);
        //     // map<uint64_t,uint64_t> m = a.submitted_ans;
        //     // m[qid] = ans;

        //     // allsubmissions.update(from,[&](submissions& b){
        //     //             b.submitted_ans = m;
        //     //         });
        // }

    
          if(!submissions.has(from)) {

             vector<uint64_t> new_qid;
             vector<uint64_t> new_ans;
             
             new_qid.push_back(qid);
             new_ans.push_back(ans);

            submissions.insert([&](submission& b){
                        b.vtr = from;
                        b.qid.assign(new_qid.begin(),new_qid.end());
                        b.ans.assign(new_ans.begin(),new_ans.end());
            });

          } else {
            
             auto a = submissions.get(from);
             vector<uint64_t> curr_q = a.qid;
             vector<uint64_t> curr_ans = a.ans;
             
             curr_q.push_back(qid);
             curr_ans.push_back(ans);

             submissions.update(from,[&](submission& b){
                        b.qid = curr_q;
                        b.ans = curr_ans; 
             });
       
          }
        
        cosio::print_f("Voting done successfully\n");


    }


    void verify_acct(uint8_t verify_level, cosio::name account){
        // make sure that only the contract owner can create verify accts
        cosio::require_auth(get_name().account());

        whitelisting.insert([&](whitelist& w){
            w.verify_level = verify_level;
            w.account = account;
        });

       cosio::print_f("Account % verified to level %\n", account, verify_level);


    }

    void createMatch(uint64_t match_id, string team1, string team2, string venue, string date){
        // make sure that only the contract owner can create live/ upcoming matches 
        cosio::require_auth(get_name().account());

        matchstats.insert([&](matchstat& m){
         m.match_id = match_id;
          m.team1 = team1;
          m.team2 = team2;
          m.venue = venue;
          m.date = date; 
        });

       cosio::print_f("New upcoming Match created with id %\n", match_id);


    }


    void submitSolution(uint64_t q_id, uint64_t right_ans){
        // make sure that only the contract owner can create live/ upcoming matches 
        cosio::require_auth(get_name().account());

        questions.update(q_id, [&](question& q){
                q.solution = right_ans;
                q.status = 2;
        });

      // immediately transfer funds to releases table of the winners
        
       // auto r = releases.get(from);
       // find total fee collected for particular qid 

       auto hold_q = holds.get(q_id);
       uint64_t total_hold_amt = hold_q.hold_amount;

        auto v = voters.get(q_id);

        vector<cosio::name> vtr =  v.vtr;
        vector<cosio::name> :: iterator it;
        for(it= vtr.begin();it!=vtr.end();it++){

            auto voter_i = submissions.get(*it);
            vector<uint64_t> voter_i_qid = voter_i.qid;

            auto it1 = std::find(voter_i_qid.begin(), voter_i_qid.end(), q_id);

            if (it1 != voter_i_qid.end())
            {
              auto index = std::distance(voter_i_qid.begin(), it1);
              vector<uint64_t> voter_i_ans = voter_i.ans;
              uint64_t submission_voter_i = voter_i_ans.at(index);

              // only right answer wins
              if(submission_voter_i == right_ans){

                // add to releases table
                // *it -> voter acc name
                cosio::print_f("Sending all funds % to winner %\n", total_hold_amt, *it);

                  if(!releases.has(*it)) {

                    releases.insert([&](release& r){
                      r.owner = *it;
                      r.amount = total_hold_amt;
                    });
                  }  
                  // modify
                  else {

                    releases.update(*it, [&](release& r){
                      r.amount += total_hold_amt;
                    });

                  }
              }
            }


        }
    }

    

    //
    // define a class member named "balances" representing a database table which,
    // - has name of "balances", the same as variable name,
    // - has a record type of balance
    // - takes balance::tokenOwner as primary key
    //
    COSIO_DEFINE_TABLE( questions, question, (q_id) );

    COSIO_DEFINE_TABLE( voters, voter, (q_id) );

    COSIO_DEFINE_TABLE( whitelisting, whitelist, (account) );

    COSIO_DEFINE_TABLE( matchstats, matchstat, (match_id) );

    COSIO_DEFINE_TABLE( submissions, submission, (vtr) );

    COSIO_DEFINE_TABLE( releases, release, (owner) );

    COSIO_DEFINE_TABLE( holds, hold, (q_id) );

  
};

// declare the class and methods of contract.
COSIO_ABI(mySport, (createquest)(vote)(verify_acct)(createMatch)(submitSolution))