import { VizualizationPage } from './app.po';

describe('vizualization App', () => {
  let page: VizualizationPage;

  beforeEach(() => {
    page = new VizualizationPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
