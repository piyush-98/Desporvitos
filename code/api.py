from pycricbuzz import Cricbuzz
import json
def scorecard(mid):
    c = Cricbuzz()
    scard = c.scorecard(mid)
    print(json.dumps(scard, indent=4, sort_keys=True))
scorecard("23952")
