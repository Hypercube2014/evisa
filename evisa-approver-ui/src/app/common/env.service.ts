export class EnvService {

  // The values that are defined here are the default values that can
  // be overridden by env.js

  // API url
  public apiUrl = window.location.href.split('#')[0];
  public build_Version='';
  public build_Date='';

  // Whether or not to enable debug mode
  public enableDebug = true;

  constructor() {
  }

}
