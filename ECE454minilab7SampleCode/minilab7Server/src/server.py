

from google.appengine.ext import db
import webapp2
import json # without this, you will have error 500

MAIN_PAGE_HTML = """\
<html>
  <body>
    <a href="storeavalue">StoreValue </a>
    </br>
    <a href="getvalue">QueryValue </a>
  </body>
</html>
"""

class StoredData(db.Model):
  tag = db.StringProperty()
  value = db.StringProperty(multiline=True)
  ## defining value as a string property limits individual values to 500
  ## characters.   To remove this limit, define value to be a text
  ## property instead, by commnenting out the previous line
  ## and replacing it by this one:
  ## value db.TextProperty()
  # this line may not be necessary
  date = db.DateTimeProperty(required=True, auto_now=True)



class StoreAValue(webapp2.RequestHandler):

  def store_a_value(self, tag, value):
    entry = db.GqlQuery("SELECT * FROM StoredData where tag = :1", tag).get()
    if entry:
        returnVal(self, lambda : json.dump(["Update"], self.response.out)) 
    else: 
        entry = StoredData(tag = tag, value = value)
        returnVal(self, lambda : json.dump(["Store"], self.response.out)) 
    entry.put()

  def post(self):
    tag = self.request.get('tag')
    value = self.request.get('value')
    self.store_a_value(tag, value)

# this is just for browser test
  def get(self):
    self.response.out.write('''
    <html><body>
    <form action="/storeavalue" method="post"
          enctype=application/x-www-form-urlencoded>
       <p>Tag<input type="text" name="tag" /></p>
       <p>Value<input type="text" name="value" /></p>
       <input type="hidden" name="fmt" value="html">
       <input type="submit" value="Store a value">
    </form></body></html>\n''')

class GetValue(webapp2.RequestHandler):

  def get_value(self, tag):
    entry = db.GqlQuery("SELECT * FROM StoredData where tag = :1", tag).get()
    if entry:
      value = entry.value
    else: value = ""
    # Python supports the creation of anonymous functions (i.e. functions that are 
    # not bound to a name) at runtime, using a construct called "lambda". 
    # http://www.secnetix.de/olli/Python/lambda_functions.hawk
    # json for python:  http://docs.python.org/2/library/json.html
    returnVal(self, lambda : json.dump(["VALUE", tag, value], self.response.out))
    
    ## The above call to returnVal is equivalent to:
    #self.response.headers['Content-Type'] = 'application/jsonrequest'
    #json.dump(["VALUE", tag, value], self.response.out)
    

  def post(self):
    # https://developers.google.com/appengine/docs/python/tools/webapp/requestclass
    tag = self.request.get('tag')
    self.get_value(tag)

  def get(self):
    self.response.out.write('''
    <html><body>
    <form action="/getvalue" method="post"
          enctype=application/x-www-form-urlencoded>
       <p>Tag<input type="text" name="tag" /></p>
       <input type="hidden" name="fmt" value="html">
       <input type="submit" value="Get value">
    </form></body></html>\n''')
    
#### Utilty procedures for generating the output
#### Handler is an appengine request handler.  writer is a thunk
#### (i.e. a procedure of no arguments) that does the write when invoked.
def returnVal(handler, writer):
    handler.response.headers['Content-Type'] = 'application/jsonrequest'
    writer()


class MainPage(webapp2.RequestHandler):
    def get(self):
    # https://developers.google.com/appengine/docs/python/tools/webapp/responseclass
        self.response.write(MAIN_PAGE_HTML)

# webapp2 overview:   http://webapp-improved.appspot.com/index.html
# Webapp2 WSGI application:
# http://webapp-improved.appspot.com/guide/app.html
application = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/storeavalue', StoreAValue),
    ('/getvalue', GetValue),
    ], debug=True)

