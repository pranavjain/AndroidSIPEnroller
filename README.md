# AndroidSIPEnroller
_{This repository contains sample application attached to it which follows the below instructions}_
* Import AndroidSIPEnroller.aar into your application -  [Link](https://github.com/pranavjain/AndroidSIPEnroller/blob/master/AndroidSIPEnroller.aar)
* Create object of SIPEnroller class		
`SIPEnroller user1 = new SIPEnroller();`
* Define value for Provider, Email, Password, DisplayName, Username and a variable to get result  
`user1.provider = "SIP2SIP";`  
`user1.email = "myemail@domain.com";`  
`user1.pass = "K33pC@lm";`  
`user1.display = "SampleDisplay_1";`  
`user1.username = "UserName.09.sample";`  
`String output;`  
* Call Enrol User Function  
`output = user1.EnrolUser(getApplicationContext());`  
* Enjoy :-)

## License  
See the [LICENSE](LICENSE.md) file for license rights and limitations (LGPL).
