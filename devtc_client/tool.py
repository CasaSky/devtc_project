import json


class Tool(object):

    def __init__(self, name):
        self.name = name

    def json(self):
        return json.dumps(self.__dict__)